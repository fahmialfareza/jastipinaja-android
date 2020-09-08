package com.dinokeylas.jastipinaja.utils

interface Constant {

    interface Collections{
        companion object{
            val USER = "user"
            val TRANSACTION = "transaction"
            val ITEM = "item"
            val BANNER_IMAGES = "bannerImages"
            val CITY = "city"
            val POST = "post"
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

    interface TransactionStatus{
        companion object{
            val ON_PROGRESS = "onProgress"
            val SUCCESS = "success"
            val FAILED = "failed"
        }
    }

    interface TransactionProgress{
        companion object{
            val ORDER = "order"
            val PAYED = "payed"
            val ON_THE_WAY = "onTheWay"
            val FINISH = "finish"
        }
    }
}