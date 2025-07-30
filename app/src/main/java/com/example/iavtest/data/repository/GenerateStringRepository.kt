package com.example.iavtest.data.repository

import android.content.ContentResolver
import android.net.Uri
import com.example.iavtest.data.model.StringModel
import org.json.JSONException
import org.json.JSONObject

class GenerateStringRepository(private val contentResolver: ContentResolver) {

    /**
     * this function is handle content provider ro get the data
     * @param maxLength y this arguments and what does
     */

    fun queryContentProvider(maxLength: Int): StringModel? {

        /**
         * The URI points to the content provider which is defined in the documentation
         */
        val uri = Uri.parse("content://com.iav.contestdataprovider/text")

        /**
         * /Query the content provider for a random string based on the max length
         */
        val cursor = contentResolver.query(
            uri,
            null,
            null,
            arrayOf(maxLength.toString()),  // Pass the maxLength as an array of Strings
            null
        )

        /**
         * / Parse the result if available the requested data else
         * return null
         */
        return cursor?.use {
            if (it.moveToFirst()) {
                val jsonString = it.getString(it.getColumnIndexOrThrow("data"))

                /**
                 *  Parse the JSONrandom string data
                 */
                val randomString = parseJson(jsonString)
                if (randomString != null && randomString.value.length > maxLength) {
                    randomString.value = randomString.value.take(maxLength)
                    randomString.length = randomString.value.length
                }
                randomString
            } else {
                null
            }
        }
    }

    /**
     * Helper function to parse the JSON string
     * returned from the content provider
     */
    private fun parseJson(jsonString: String): StringModel? {
        return try {
            val jsonObject = JSONObject(jsonString).getJSONObject("randomText")
            StringModel(
                value = jsonObject.getString("value"),
                length = jsonObject.getInt("length"),
                created = jsonObject.getString("created")
            )
        } catch (e: JSONException) {
            null
        }
    }
}