package com.xily.dmzj2.data.remote

import com.xily.dmzj2.data.remote.service.DmzjApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RetrofitHelper(private val dmzjApi: DmzjApiService) : HttpHelper {
    override suspend fun getHotComments(comicId: String, page: String) =
        withContext(Dispatchers.IO) {
            dmzjApi.getHotComments(comicId, page)
        }

    override suspend fun getTopComment(comicId: String) = withContext(Dispatchers.IO) {
        dmzjApi.getTopComment(comicId)
    }

    override suspend fun getComments(comicId: String, page: String, limit: String) =
        withContext(Dispatchers.IO) {
            dmzjApi.getComments(comicId, page, limit)
        }

    override suspend fun getReadInfo(uid: String, comicId: String) = withContext(Dispatchers.IO) {
        dmzjApi.getReadInfo(uid, comicId)
    }

    override suspend fun getSubscribeStatus(uid: String, comicId: String) =
        withContext(Dispatchers.IO) {
            dmzjApi.getSubscribeStatus(uid, comicId)
        }

    override suspend fun recordRead(queryMap: Map<String, String>) = withContext(Dispatchers.IO) {
        dmzjApi.recordRead(queryMap)
    }

    override suspend fun getLatest(type: String, page: String) = withContext(Dispatchers.IO) {
        dmzjApi.getLatest(type, page)
    }

    override suspend fun getSubject(page: String) = withContext(Dispatchers.IO) {
        dmzjApi.getSubject(page)
    }

    override suspend fun getCategory() = withContext(Dispatchers.IO) {
        dmzjApi.getCategory()
    }

    override suspend fun getClassifyFilter() = withContext(Dispatchers.IO) {
        dmzjApi.getClassifyFilter()
    }

    override suspend fun getClassify(filter: String, page: String) = withContext(Dispatchers.IO) {
        dmzjApi.getClassify(filter, page)
    }

    override suspend fun getRecommend() = withContext(Dispatchers.IO) {
        dmzjApi.getRecommend()
    }

    override suspend fun getMineRecommend(categoryId: String, uid: String) =
        withContext(Dispatchers.IO) {
            dmzjApi.getMineRecommend(categoryId, uid)
        }

    override suspend fun getRankFilter() = withContext(Dispatchers.IO) {
        dmzjApi.getRankFilter()
    }

    override suspend fun getRank(
        filter: String,
        day: String,
        type: String,
        page: String
    ) = withContext(Dispatchers.IO) {
        dmzjApi.getRank(filter, day, type, page)
    }

    override suspend fun getComic(comicId: String) = withContext(Dispatchers.IO) {
        dmzjApi.getComic(comicId)
    }

    override suspend fun getChapter(comicId: String, chapterId: String) =
        withContext(Dispatchers.IO) {
            dmzjApi.getChapter(comicId, chapterId)
        }

    override suspend fun getSearchHot() = withContext(Dispatchers.IO) {
        dmzjApi.getSearchHot()
    }

    override suspend fun getViewPoint(comicId: String, chapterId: String) =
        withContext(Dispatchers.IO) {
            dmzjApi.getViewPoint(comicId, chapterId)
        }

    override suspend fun login(nickname: String, password: String) = withContext(Dispatchers.IO) {
        dmzjApi.login(nickname, password)
    }

    override suspend fun getHistory(uid: String) = withContext(Dispatchers.IO) {
        dmzjApi.getHistory(uid)
    }

    override suspend fun search(word: String, page: String) = withContext(Dispatchers.IO) {
        dmzjApi.search(word, page)
    }

    override suspend fun getUserInfo(uid: String, token: String) = withContext(Dispatchers.IO) {
        dmzjApi.getUserInfo(uid, token)
    }

    override suspend fun getSubscribe(
        uid: String,
        token: String,
        page: String
    ) = withContext(Dispatchers.IO) {
        dmzjApi.getSubscribe(uid, token, page)
    }

    override suspend fun checkVersion() = withContext(Dispatchers.IO) {
        dmzjApi.checkVersion()
    }

}
