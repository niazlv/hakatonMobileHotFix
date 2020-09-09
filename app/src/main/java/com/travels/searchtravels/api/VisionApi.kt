package com.travels.searchtravels.api

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.vision.v1.Vision
import com.google.api.services.vision.v1.model.AnnotateImageRequest
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest
import com.google.api.services.vision.v1.model.Feature
import com.travels.searchtravels.utils.ImageHelper
import java.io.IOException
import java.util.*

object VisionApi {
    fun findLocation(
        bitmap: Bitmap?,
        token: String?,
        onVisionApiListener: OnVisionApiListener
    ) {
        val handler = Handler(Looper.getMainLooper())
        val thread = Thread(Runnable {
            try {
                val credential = GoogleCredential().setAccessToken(token)
                val httpTransport = AndroidHttp.newCompatibleTransport()
                val jsonFactory: JsonFactory =
                    GsonFactory.getDefaultInstance()
                val builder =
                    Vision.Builder(httpTransport, jsonFactory, credential)
                val vision = builder.build()
                val featureList: MutableList<Feature> =
                    ArrayList()
                val textDetection =
                    Feature()
                textDetection.type = "WEB_DETECTION"
                textDetection.maxResults = 10
                featureList.add(textDetection)

                val landmarkDetection =
                    Feature()
                landmarkDetection.type = "LANDMARK_DETECTION"
                landmarkDetection.maxResults = 10
                featureList.add(landmarkDetection)

                val imageList: MutableList<AnnotateImageRequest> =
                    ArrayList()
                val annotateImageRequest = AnnotateImageRequest()
                val base64EncodedImage =
                    ImageHelper.getBase64EncodedJpeg(bitmap)
                annotateImageRequest.image = base64EncodedImage
                annotateImageRequest.features = featureList
                imageList.add(annotateImageRequest)
                val batchAnnotateImagesRequest =
                    BatchAnnotateImagesRequest()
                batchAnnotateImagesRequest.requests = imageList
                val annotateRequest =
                    vision.images().annotate(batchAnnotateImagesRequest)
                // Due to a bug: requests to Vision API containing large images fail when GZipped.
                annotateRequest.disableGZipContent = true
                Log.d("VISION_API", "sending request")
                val response = annotateRequest.execute()
                try {
                    if (response != null && response.responses != null && response.responses[0] != null && response.responses[0]
                            .landmarkAnnotations != null && response.responses[0].landmarkAnnotations[0] != null && response.responses[0]
                            .landmarkAnnotations[0]
                            .locations != null && response.responses[0]
                            .landmarkAnnotations[0].locations[0] != null && response.responses[0]
                            .landmarkAnnotations[0].locations[0]
                            .latLng != null
                    ) {
                        handler.post {
                            onVisionApiListener.onSuccess(
                                response.responses[0].landmarkAnnotations[0].locations[0].latLng
                            )
                        }
                    } else if (response != null) {
                        if (response.toString().toLowerCase().contains("\"sea\"")) {
                            handler.post { onVisionApiListener.onErrorPlace("sea") }
                        } else if (response.toString().toLowerCase().contains("\"beach\"")) {
                            handler.post { onVisionApiListener.onErrorPlace("beach") }
                        } else if (response.toString().toLowerCase().contains("\"mountain\"")) {
                            handler.post { onVisionApiListener.onErrorPlace("mountain") }
                        } else if (response.toString().toLowerCase().contains("\"snow\"")) {
                            handler.post { onVisionApiListener.onErrorPlace("snow") }
                        } else if (response.toString().toLowerCase().contains("\"ocean\"")) {
                            handler.post { onVisionApiListener.onErrorPlace("ocean") }
                        } else {
                            handler.post { onVisionApiListener.onError() }
                        }
                    }
                    println("Cloud Vision success = $response")
                } catch (e: Error) {
                    e.printStackTrace()
                    handler.post { onVisionApiListener.onError() }
                }
            } catch (e: GoogleJsonResponseException) {
                handler.post { onVisionApiListener.onError() }
                Log.e("VISION_API", "Request failed: " + e.content)
            } catch (e: IOException) {
                handler.post { onVisionApiListener.onError() }
                Log.d("VISION_API", "Request failed: " + e.message)
            }
        })
        thread.start()
    }
}