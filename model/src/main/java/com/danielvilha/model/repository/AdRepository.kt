package com.danielvilha.model.repository

import android.util.Log
import com.danielvilha.model.Ad
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AdRepository @Inject constructor(
    firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val adsCollection = firestore.collection("ads")

    suspend fun loadAdById(id: String): AdResult {
        return try {
            val doc = adsCollection.document(id).get().await()
            if (doc.exists()) {
                val ad = doc.toObject(Ad::class.java)?.copy(id = doc.id)
                if (ad != null) AdResult.Success(ad)
                else AdResult.Failure("Ad could not be parsed")
            } else {
                AdResult.Failure("Ad not found")
            }
        } catch (e: Exception) {
            AdResult.Failure(e.message ?: "Unknown error")
        }
    }

    suspend fun createOrUpdateAd(ad: Ad): AdResult {
        return try {
            val userId = auth.currentUser?.uid ?: return AdResult.Failure("User not logged in")
            val adToSave = ad.copy(userId = userId)

            val docRef = if (ad.id?.isBlank() == true) {
                adsCollection.document()
            } else {
                adsCollection.document(ad.id.toString())
            }

            docRef.set(adToSave.copy(id = docRef.id)).await()
            AdResult.Success(adToSave.copy(id = docRef.id))
        } catch (e: Exception) {
            AdResult.Failure(e.message ?: "Unknown error")
        }
    }

    suspend fun loadAds(): AdResult {
        return try {
            val ads = adsCollection
                .whereEqualTo("visible", true)
                .get()
                .await()
                .documents.mapNotNull { doc ->
                    doc.toObject(Ad::class.java)?.copy(id = doc.id)
                }
            AdResult.SuccessList(ads)
        } catch (e: Exception) {
            Log.e("LoadAds", "loadAds: ", e)
            AdResult.Failure(e.message ?: "Unknown error")
        }
    }

    suspend fun loadMyAds(userId: String): AdResult {
        return try {
            val ads = adsCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .documents.mapNotNull { doc ->
                    doc.toObject(Ad::class.java)?.copy(id = doc.id)
                }
            AdResult.SuccessList(ads)
        } catch (e: Exception) {
            Log.e("LoadMyAds", "loadAds: ", e)
            AdResult.Failure(e.message ?: "Unknown error")
        }
    }
}