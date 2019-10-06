package com.xily.dmzj2.data.remote.service

import com.xily.dmzj2.data.remote.ApiConfig
import com.xily.dmzj2.data.remote.model.*
import retrofit2.http.*

interface DmzjApiService {
    @GET(ApiConfig.myApiUrl + "checkVersion3")
    suspend fun checkVersion(): VersionBean

    @GET(ApiConfig.dmzjApiUrl + "comic/comic_{comicId}.json")
    suspend fun getComic(@Path("comicId") comicId: String): ComicBean

    @GET(ApiConfig.dmzjApiUrl + "chapter/{comicId}/{chapterId}.json")
    suspend fun getChapter(@Path("comicId") comicId: String, @Path("chapterId") chapterId: String): ChapterBean

    @GET(ApiConfig.dmzjApiUrl + "search/hot/0.json")
    suspend fun getSearchHot(): List<SearchHotBean>

    @GET(ApiConfig.dmzjApiUrl + "viewPoint/0/{comicId}/{chapterId}.json")
    suspend fun getViewPoint(@Path("comicId") comicId: String, @Path("chapterId") chapterId: String): ViewPointBean

    @POST(ApiConfig.dmzjUserUrl + "loginV2/m_confirm")
    @FormUrlEncoded
    suspend fun login(@Field("nickname") nickname: String, @Field("passwd") password: String): LoginBean

    @GET(ApiConfig.dmzjApiUrl2 + "getReInfo/comic/{uid}/0")
    suspend fun getHistory(@Path("uid") uid: String): HistoryBean

    @GET(ApiConfig.dmzjApiUrl + "search/show/0/{word}/{page}.json")
    suspend fun search(@Path("word") word: String, @Path("page") page: String): List<SearchBean>

    @GET(ApiConfig.dmzjApiUrl + "UCenter/comicsv2/{uid}.json")
    suspend fun getUserInfo(@Path("uid") uid: String, @Query("dmzj_token") token: String): UserBean

    @GET(ApiConfig.dmzjApiUrl + "UCenter/subscribe?sub_type=1&letter=all&type=0")
    suspend fun getSubscribe(@Query("uid") uid: String, @Query("dmzj_token") token: String, @Query("page") page: String): List<SubscribeBean>

    @GET(ApiConfig.dmzjCommentUrl + "v1/4/latest/{comicId}")
    suspend fun getComments(
        @Path("comicId") comicId: String, @Query("page_index") page: String, @Query(
            "limit"
        ) limit: String
    )

    @GET(ApiConfig.dmzjApiUrl + "latest/{type}/{page}.json")
    suspend fun getLatest(@Path("type") type: String, @Path("page") page: String): List<LatestBean>

    @GET(ApiConfig.dmzjApiUrl + "subject/0/{page}.json")
    suspend fun getSubject(@Path("page") page: String): List<SubjectBean>

    @GET(ApiConfig.dmzjApiUrl + "/0/category.json")
    suspend fun getCategory(): List<CategoryBean>

    @GET(ApiConfig.dmzjApiUrl + "classify/filter.json")
    suspend fun getClassifyFilter(): List<ClassifyFilterBean>

    @GET(ApiConfig.dmzjApiUrl + "classify/{filter}/{type}/{page}.json")
    suspend fun getClassify(@Path("filter") filter: String, @Path("page") page: String): List<ClassifyBean>

    @GET(ApiConfig.dmzjApiUrl + "recommend.json")
    suspend fun getRecommend(): List<RecommendBean>

    @GET(ApiConfig.dmzjApiUrl + "recommend/batchUpdate")
    suspend fun getMineRecommend(@Query("category_id") categoryId: String, @Query("uid") uid: String): MineRecommendBean

    @GET(ApiConfig.dmzjApiUrl + "rank/type_filter.json")
    suspend fun getRankFilter(): List<RankFilterBean>

    @GET(ApiConfig.dmzjApiUrl + "rank/{filter}/{day}/{type}/{page}.json")
    suspend fun getRank(
        @Path("filter") filter: String, @Path("day") day: String, @Path("type") type: String, @Path(
            "page"
        ) page: String
    ): List<RankBean>

    @GET(ApiConfig.dmzjApiUrl2 + "getReInfo/comic/{uid}/{comicId}/0")
    suspend fun getReadInfo(@Path("uid") uid: String, @Path("comicId") comicId: String): ReInfoBean

    @GET(ApiConfig.dmzjApiUrl + "subscribe/0/{uid}/{comicId}")
    suspend fun getSubscribeStatus(@Path("uid") uid: String, @Path("comicId") comicId: String): SubscribeStatusBean

    @GET(ApiConfig.dmzjApiUrl2 + "record/getRe")
    suspend fun recordRead(@QueryMap queryMap: Map<String, String>): RecordReadBean
}