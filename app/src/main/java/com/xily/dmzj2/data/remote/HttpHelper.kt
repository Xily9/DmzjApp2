package com.xily.dmzj2.data.remote

import com.xily.dmzj2.data.remote.model.*
import okhttp3.ResponseBody


interface HttpHelper {

    suspend fun checkVersion(): VersionBean
    suspend fun getComic(comicId: String): ComicBean
    suspend fun getChapter(comicId: String, chapterId: String): ChapterBean
    suspend fun getSearchHot(): List<SearchHotBean>
    suspend fun getViewPoint(comicId: String, chapterId: String): ViewPointBean
    suspend fun login(nickname: String, password: String): LoginBean
    suspend fun getHistory(uid: String): List<HistoryBean>
    suspend fun search(word: String, page: String): List<SearchBean>
    suspend fun getUserInfo(uid: String, token: String): UserBean
    suspend fun getSubscribe(uid: String, token: String, page: String): List<SubscribeBean>
    suspend fun getLatest(type: String, page: String): List<LatestBean>
    suspend fun getSubject(page: String): List<SubjectBean>
    suspend fun getCategory(): List<CategoryBean>
    suspend fun getClassifyFilter(): List<ClassifyFilterBean>
    suspend fun getClassify(filter: String, page: String): List<ClassifyBean>
    suspend fun getRecommend(): List<RecommendBean>
    suspend fun getMineRecommend(categoryId: String, uid: String): MineRecommendBean
    suspend fun getRankFilter(): List<RankFilterBean>
    suspend fun getRank(filter: String, day: String, type: String, page: String): List<RankBean>
    suspend fun getReadInfo(uid: String, comicId: String): ReInfoBean
    suspend fun getSubscribeStatus(uid: String, comicId: String): SubscribeStatusBean
    suspend fun recordRead(queryMap: Map<String, String>): RecordReadBean
    suspend fun getTopComment(comicId: String): TopCommentBean
    suspend fun getComments(comicId: String, page: String, limit: String): CommentBean
    suspend fun getHotComments(comicId: String, page: String): ResponseBody
}
