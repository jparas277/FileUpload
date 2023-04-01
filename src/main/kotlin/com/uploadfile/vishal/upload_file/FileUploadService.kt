package com.uploadfile.vishal.upload_file

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import reactor.core.publisher.Mono
import org.springframework.http.*
import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.util.unit.DataSize
import java.io.File

import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.naming.SizeLimitExceededException
enum class FileFormat {
    jpeg, pdf, csv, xls, doc, png
}
@Service
class FileUploadService {

    companion object {
        //val DIRECTORY = System.getProperty("user.home") + "//Desktop//FileUploadDownload//upload//"
        val DIRECTORY = System.getenv("UPLOAD_DIRECTORY") ?: "/app/upload/"
    }
    private val fileStorage = Paths.get(DIRECTORY).toAbsolutePath().normalize()
    fun uploadFile(filePartMono: FilePart, contentLength: Long): Mono<ResponseEntity<String>> {
        return Mono.just(filePartMono).flatMap { fp: FilePart ->

            val supportedFormats = enumValues<FileFormat>().map { it.name}
            when {
                contentLength <= 174L -> Mono.error(FileNotFoundException())
                contentLength > DataSize.ofKilobytes(5000).toBytes() -> Mono.error(SizeLimitExceededException())
                fp.filename().substring(fp.filename().lastIndexOf('.') + 1) !in supportedFormats -> Mono.error(UnsupportedFormat())
                else -> {
                    fp.transferTo(fileStorage.resolve(fp.filename()))
                        .then(Mono.just(ResponseEntity.ok().body("/download/${fp.filename()}")))
                }
            }
        }
    }
    fun downloadFile( filename: String): Mono<ResponseEntity<Resource>> {
        val filePath: Path = fileStorage.toAbsolutePath().normalize().resolve(filename)
        return Mono.fromCallable {
            when {
                !Files.exists(filePath) -> throw FileNotFoundException()
                else -> {
                    val resource: Resource = UrlResource(filePath.toUri())
                    val httpHeaders = HttpHeaders()
                    httpHeaders.add("File-Name", filename)
                    httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.filename)
                    ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                        .headers(httpHeaders).body(resource)
                }
            }
        }
    }
}
//download without reactive

//get mapping without validation
//response.getHeaders()[HttpHeaders.CONTENT_DISPOSITION] = "attachment; filename=$fileName"
//response.getHeaders().contentType = MediaType.APPLICATION_OCTET_STREAM
//val resource = UrlResource(fileStorage.toAbsolutePath().normalize().resolve(fileName).toUri())
//val file = resource.file
//return zeroResponse.writeWith(file, 0, file.length())