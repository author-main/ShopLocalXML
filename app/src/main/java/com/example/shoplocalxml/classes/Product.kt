package com.example.shoplocalxml.classes

import android.graphics.Bitmap
import com.example.shoplocalxml.R
import com.example.shoplocalxml.getStringResource
import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name:           String,
    @SerializedName("category")
    var category:       Int? = null,
    @SerializedName("description")
    var description:    String,
    @SerializedName("instock")
    var instock:        Int = 1,
    @SerializedName("discount")
    var discount:       Int,
    @SerializedName("price")
    var price:          Float,
    @SerializedName("star")
    var star:           Float = 1.0f,
    @SerializedName("favorite")
    var favorite:       Byte = 0,
    @SerializedName("brand")
    var brand:          Int? = null,
    @SerializedName("sold")
    var sold:          Int? = null,
    @SerializedName("linkimages")
    var linkimages:     List<String>? = null,
   // var imageindex: Int = 0
    ) {
        constructor(): this(
                id              = -1,
                name            = "",
                category        = 0,
                description     = "",
                instock         = 0,
                discount        = 0,
                price           = 0.0f,
                star            = 1.0f,
                favorite        = 0,
                brand           = 0,
                sold            = 0,
                linkimages      =  emptyList<String>()
        )

    /**
 * // Структура таблицы PRODUCTS //
 * `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
 * `name` VARCHAR(50) NOT NULL,
 * `category` INT UNSIGNED NULL,
 * `description` TEXT NOT NULL,
 * `instock` INT NOT NULL DEFAULT 1,
 * `price` DECIMAL(10,2) UNSIGNED NOT NULL DEFAULT 0.00,
 * `deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
 * `star` TINYINT NULL DEFAULT 1,
 * `brand` INT UNSIGNED NULL,
 * FOREIGN KEY (`category`) REFERENCES `shop_local`.`category` (`id`)
 * FOREIGN KEY (`brand`) REFERENCES `shop_local`.`brands` (`id`)
 */
    fun getTypeSale() =
        if (star >= 4)
            getStringResource(R.string.text_bestseller)
        else if (discount > 0)
            getStringResource(R.string.text_action)
        else
            ""
    fun copydata(value: Product){
        id              = value.id
        name            = value.name
        category        = value.category
        description     = value.description
        instock         = value.instock
        discount        = value.discount
        price           = value.price
        star            = value.star
        favorite        = value.favorite
        brand           = value.brand
        sold            = value.sold
        linkimages      = value.linkimages
    }

 }