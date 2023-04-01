package com.uploadfile.vishal.upload_file

//import org.springframework.http.ResponseEntity
//import org.springframework.util.StringUtils
//import org.springframework.web.bind.annotation.PostMapping
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RequestParam
//import org.springframework.web.bind.annotation.RestController
//import org.springframework.web.multipart.MultipartFile
//import java.io.IOException
//import java.nio.file.Path
//import java.nio.file.Paths

import org.springframework.core.io.Resource
import org.springframework.http.*
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.io.IOException

@RestController
class FileUploadController(private val fileUploadService: FileUploadService) {

    @PostMapping("/upload")
    fun uploadFile(
        @RequestPart("myFile") filePartMono: FilePart,
        @RequestHeader("Content-Length") contentLength: Long
    ): Mono<ResponseEntity<String>> {
        return fileUploadService.uploadFile(filePartMono, contentLength)
    }

    @GetMapping("/download/{fileName}")
    @Throws(IOException::class)
    fun downloadFile(@PathVariable fileName: String): Mono<ResponseEntity<Resource>> {
        return fileUploadService.downloadFile(fileName)
    }
}

//
//@RestController
////@RequestMapping("file")
//public open class FileUploadController {
//
//    companion object {
//        val DIRECTORY = System.getProperty("user.home") + "/Downloads/Rapipay_Project/kotlin/upload_file/upload/"
//    }
//
//    val fileStorage = Paths.get(DIRECTORY).toAbsolutePath().normalize()
//    @PostMapping("/upload")
//    fun uploadFile(@RequestPart("myFile")  filePartMono: Mono<FilePart>, @RequestHeader("Content-Length") contentLength:Long): Mono<Any> {
//        var fileName = filePartMono;
//        println(fileName)
//        return filePartMono
//            .flatMap { fp: FilePart ->
//                val index = fp.filename().lastIndexOf('.')
//                val format = fp.filename().substring(index + 1)
//                when {
//                    contentLength <= 174L -> Mono.error(FileNotFoundException("Please select a non-empty file"))
//                    contentLength > DataSize.ofKilobytes(5000)
//                        .toBytes() -> Mono.error(SizeLimitExceededException("File Size is too large! please upload less than 5MB"))
//                    contentLength == DataSize.ofKilobytes(0)
//                        .toBytes() -> Mono.error(SizeLimitExceededException("File Size is too low! please upload greater than 0MB"))
//                    format !in setOf("jpeg", "pdf", "csv", "xls", "doc", "png")
//                    -> Mono.error(UnsupportedFormatException("$format file format not allowed"))
//                    else -> {
//                        fp.transferTo(fileStorage.resolve(fp.filename()))
//                            .then(Mono.just(ResponseEntity.ok().body("/download/${fp.filename()}")))
//                    }
//                }
//            }
//    }
//    @GetMapping("/download/{fileName}")
//    @Throws(IOException::class)
//    fun downloadFile(@PathVariable fileName: String, response: ServerHttpResponse): Mono<Void> {
//        val zeroResponse = response as ZeroCopyHttpOutputMessage
//        response.getHeaders()[HttpHeaders.CONTENT_DISPOSITION] = "attachment; filename=$fileName"
//        response.getHeaders().contentType = MediaType.APPLICATION_OCTET_STREAM
//        val resource = UrlResource(fileStorage.toAbsolutePath().normalize().resolve(fileName).toUri())
//        val file = resource.file
//        return zeroResponse.writeWith(file, 0, file.length())
//    }
//}

//in controller all functionality



////single file upload
//@PostMapping("single/upload")
//fun uploadFile(@RequestPart("myFile") filePartMono: Mono<FilePart>): Mono<Void> {
//    return filePartMono
//        .doOnNext { fp: FilePart -> println("Received File : " + fp.filename())}
//        .flatMap { fp: FilePart -> fp.transferTo(fileStorage.resolve(fp.filename()))}
//        .then()
//}
//
////multiple file upload
//@PostMapping("multi/upload")
//fun uploadMultipleFiles(@RequestPart("myFiles") partFlux: Flux<FilePart>): Mono<Void> {
//    return partFlux
//        .doOnNext { fp: FilePart -> println(fp.filename()) }
//        .flatMap { fp: FilePart -> fp.transferTo(fileStorage.resolve(fp.filename())) }
//        .then()
//}
//companion object {
//    val DIRECTORY = System.getProperty("user.home") + "/Downloads/Rapipay_Project/kotlin/upload_file/upload/"
//}

//webflux project working
//@PostMapping("single/upload")
//fun uploadFile(@RequestPart("myFile") filePartMono: Mono<FilePart>): Mono<Void> {
//    return filePartMono
//        .doOnNext { fp: FilePart -> println("Received File : " + fp.filename())}
//        .flatMap { fp: FilePart -> fp.transferTo(fileStorage.resolve(fp.filename()))}
//        .then()
//}
//
////multiple file upload
//@PostMapping("multi/upload")
//fun uploadMultipleFiles(@RequestPart("myFiles") partFlux: Flux<FilePart>): Mono<Void> {
//    return partFlux
//        .doOnNext { fp: FilePart -> println(fp.filename()) }
//        .flatMap { fp: FilePart -> fp.transferTo(fileStorage.resolve(fp.filename())) }
//        .then()
//}


//return@flatMap fp.transferTo(fileStorage.resolve(fp.filename()))