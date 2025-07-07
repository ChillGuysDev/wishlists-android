package com.nikol.wishlist.core.data.network.friends


//interface FriendsApiService {
//
//    @GET("/friends")
//    suspend fun getFriends(): List<Friend>
//
//    @POST("/friends/requests")
//    suspend fun createFriendRequest(@Body request: FriendRequestBody): FriendRequestResponse
//
//    @GET("/friends/requests/incoming")
//    suspend fun getIncomingFriendRequests(): List<FriendRequest>
//
//    @GET("/friends/requests/outgoing")
//    suspend fun getOutgoingFriendRequests(): List<FriendRequest>
//
//    @POST("/friends/requests/{request_id}/accept")
//    suspend fun acceptFriendRequest(@Path("request_id") requestId: String): FriendRequestResponse
//
//    @POST("/friends/requests/{request_id}/decline")
//    suspend fun declineFriendRequest(@Path("request_id") requestId: String): FriendRequestResponse
//
//    @DELETE("/friends/{friend_id}")
//    suspend fun removeFriend(@Path("friend_id") friendId: String): DeleteResponse
//}