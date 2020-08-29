package com.dinokeylas.jastipinaja.utils

interface Constant {

    interface Collections{
        companion object{
            val USER = "user"
            val TRANSACTION = "transaction"
            val ITEM = "item"
            val BANNER_IMAGES = "bannerImages"
            val CITY = "city"
        }
    }

    interface PostType{
        companion object{
            val POST_PRODUCT = 0
            val REQUEST_PRODUCT = 1
        }
    }

    interface ProductCategory{
        companion object{
            val GENERAL = 0
        }
    }

}