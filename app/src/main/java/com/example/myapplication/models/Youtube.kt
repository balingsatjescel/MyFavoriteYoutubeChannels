package com.example.myapplication.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Youtube(var id: String? = "", var Cname: String? = "", var Clink: String? = "", var Crank: String? = "", var reason: String? = "") {
    override fun toString(): String {
        return "$Cname on $Clink ranked as $Crank because $reason"
    }
}