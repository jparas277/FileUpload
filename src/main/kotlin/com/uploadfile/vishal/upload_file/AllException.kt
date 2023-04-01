package com.uploadfile.vishal.upload_file

class FileNotFound(message: String?=null, cause: Throwable?= null): Exception(message, cause){
    constructor(cause: Throwable):this(null, cause)
}
class SizeLimitExceeded(message: String?=null, cause: Throwable?=null): Exception(message, cause){
    constructor(cause: Throwable):this(null, cause)
}
class UnsupportedFormat(message: String?=null, cause: Throwable?=null): Exception(message, cause){
    constructor(cause: Throwable):this(null, cause)
}