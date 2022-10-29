package com.example.sklad.signInPhone

import android.os.Parcel
import android.os.Parcelable

    class AuthModel private constructor(`in`: Parcel) : Parcelable {
        private val clientId: String?
        private val clientSecret: String?
        private val redirectUri: String?
        private val scopes: List<String>?
        private val state: String?
        private val note: String?
        private val noteUrl: String?

        private val otpCode: String?
        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(clientId)
            dest.writeString(clientSecret)
            dest.writeString(redirectUri)
            dest.writeStringList(scopes)
            dest.writeString(state)
            dest.writeString(note)
            dest.writeString(noteUrl)
            dest.writeString(otpCode)
        }
//
//        companion object {
//            val CREATOR: Parcelable.Creator<AuthModel> = object : Parcelable.Creator<AuthModel?> {
//                override fun createFromParcel(source: Parcel): AuthModel? {
//                    return AuthModel(source)
//                }
//
//                override fun newArray(size: Int): Array<AuthModel?> {
//                    return arrayOfNulls(size)
//                }
//            }
//        }

        init {
            clientId = `in`.readString()
            clientSecret = `in`.readString()
            redirectUri = `in`.readString()
            scopes = `in`.createStringArrayList()
            state = `in`.readString()
            note = `in`.readString()
            noteUrl = `in`.readString()
            otpCode = `in`.readString()
        }

        companion object CREATOR : Parcelable.Creator<AuthModel> {
            override fun createFromParcel(parcel: Parcel): AuthModel {
                return AuthModel(parcel)
            }

            override fun newArray(size: Int): Array<AuthModel?> {
                return arrayOfNulls(size)
            }
        }
    }

