package com.savan.mytask

import com.google.gson.annotations.SerializedName

//data class Posts(
//
//	@field:SerializedName("Posts")
//	val posts: List<PostsItem?>? = null
//)

data class Posts(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("body")
	val body: String? = null,

	@field:SerializedName("userId")
	val userId: Int? = null
)
