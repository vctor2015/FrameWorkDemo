package com.centanet.frameworkdemo.api;

import com.centanet.frameworkdemo.model.GHResponse;
import com.centanet.frameworkdemo.model.businessobject.MeiZiBO;
import com.centanet.frameworkdemo.model.businessobject.SearchBO;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by vctor2015 on 2016/11/17.
 * <p>
 * 请求接口
 */

@SuppressWarnings("all")
public interface GHApi {


    /**
     * 干货资源
     *
     * @param source 资源分类{@link com.centanet.frameworkdemo.enums.GHCategory}
     * @param page   页码，1开始
     * @see <a href="http://gank.io/api">干活集中营api</a>
     */
    @GET("data/{source}/10/{page}")
    Observable<GHResponse<ArrayList<MeiZiBO>>> category(
            @Path("source") String source,
            @Path("page") int page);

    /**
     * 干货资源
     *
     * @param source   资源分类{@link com.centanet.frameworkdemo.enums.GHCategory}
     * @param pagesize 每页数量
     * @param page     页码，1开始
     * @see <a href="http://gank.io/api">干活集中营api</a>
     */
    @GET("data/{source}/{pagesize}/{page}")
    Observable<GHResponse<ArrayList<MeiZiBO>>> category(
            @Path("source") String source,
            @Path("pagesize") int pagesize,
            @Path("page") int page);

    /**
     * 搜索
     *
     * @param query 搜索内容
     * @see <a href="http://gank.io/api">干活集中营api</a>
     */
    @GET("search/query/{query}/category/all/count/1/page/1")
    Observable<GHResponse<ArrayList<SearchBO>>> search(@Path("query") String query);

    /**
     * 搜索
     *
     * @param page 页码
     * @see <a href="http://gank.io/api">干活集中营api</a>
     */
    @GET("search/query/ListView/category/all/count/10/page/{page}")
    Observable<GHResponse<ArrayList<SearchBO>>> search(@Path("page") int page);
}
