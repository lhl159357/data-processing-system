import axios, {AxiosError} from "axios";
import type { AxiosInstance } from 'axios';
import type { AxiosResponse } from 'axios';
import type { InternalAxiosRequestConfig } from 'axios';


const config = {
    baseURL: "http://localhost:9090",
    timeout: 10000,
    withCredentials: true,
    headers: {},
};

class RequestHttp {
    service: AxiosInstance;

    constructor() {
        this.service = axios.create(config);

        /**
         * @description 请求拦截器
         */
        this.service.interceptors.request.use(
            (config: InternalAxiosRequestConfig) => {
                config.headers['Content-Type'] = 'application/json;charset=utf-8';
                const userString = localStorage.getItem("user");
                const user = userString ? JSON.parse(userString) : null;
                if (user && user.token){
                    config.headers['token'] = user.token;  // 设置请求头
                }
                return config;
            },
            error => {
                // 对错误进行可能的处理，然后返回拒绝的 Promise
                return Promise.reject(error);
            }
        );

        /**
         * @description 响应拦截器
         */
        this.service.interceptors.response.use(
            (response: AxiosResponse) => {
                const { data } = response;
                return data;
            },

            (error: AxiosError) => {
                const { response } = error;
                if (response) {
                    checkStatus(response.status);
                }
                return false;
            }
        );
    }

    // 常用请求方法封装
    get(url: string, params?: object, _object = {}) {
        return this.service.get(url, { params, ..._object });
    }
    post(url: string, params?: object, _object = {}) {
        return this.service.post(url, params, _object);
    }
    put(url: string, params?: object, _object = {}) {
        return this.service.put(url, params, _object);
    }
    delete(url: string, params?: any, _object = {}) {
        return this.service.delete(url, { params, ..._object });
    }
}

/**
 * @description: 校验网络请求状态码
 * @param {Number} status
 * @return void
 */
const checkStatus = (status: number): void => {
    switch (status) {
        case 404:
            console.warn("资源不存在！");
            break;
        case 405:
            console.warn("请求方式错误！");
            break;
        case 500:
            console.warn("服务器异常！");
            break;
        default:
            console.warn("请求失败！");
    }
};

const request = new RequestHttp();
export default request;
