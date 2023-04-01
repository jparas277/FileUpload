package com.uploadfile.vishal.upload_file

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.util.unit.DataSize
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.io.FileNotFoundException
import java.nio.file.Path
import javax.naming.SizeLimitExceededException


@ExtendWith(MockitoExtension::class)
class InjectTextResourcesTests() {

	@InjectMocks
	private lateinit var service:FileUploadService;

	@Test
	fun testFile() {
		val filePart: FilePart = mock(FilePart::class.java)
		Mockito.`when`(filePart.filename()).thenReturn("TestImage.png")
		Mockito.`when`(filePart.transferTo(Mockito.any(Path::class.java))).thenReturn(Mono.empty())
		var response:Mono<ResponseEntity<String>> = service.uploadFile(filePart, DataSize.ofKilobytes(5000).toBytes()).doOnNext {
			p -> println(p.body)
		}
		StepVerifier.create(response)
			.expectNextMatches{obj -> obj.statusCode.is2xxSuccessful}
			.verifyComplete()
	}
	@Test
	fun fileLimitExceeded(){
		val filePart: FilePart = mock(FilePart::class.java)
		var response:Mono<ResponseEntity<String>> = service.uploadFile(filePart, DataSize.ofKilobytes(100000).toBytes())
		StepVerifier.create(response)
			.verifyError(SizeLimitExceededException::class.java)
	}
	@Test
	fun testFileNotFound() {
		var response: Mono<ResponseEntity<Resource>> = service.downloadFile("not_exist.pdf")
		StepVerifier.create(response)
			.expectErrorMatches{ it is FileNotFoundException }
			.verify()
	}
	@Test
	fun testFileFound() {
		var response: Mono<ResponseEntity<Resource>> = service.downloadFile("images.png")
		StepVerifier.create(response)
			.expectNextMatches{obj->obj.statusCode.is2xxSuccessful}
			.verifyComplete()
	}
	@Test
	fun testDownloadFile() {
			val filePart: FilePart = mock(FilePart::class.java)
			Mockito.`when`(filePart.filename()).thenReturn("TestImage.zip")
			var response:Mono<ResponseEntity<String>> = service.uploadFile(filePart, DataSize.ofKilobytes(5000).toBytes()).doOnNext {
					p -> println(p.body)
			}
			StepVerifier.create(response)
				.verifyError(UnsupportedFormat::class.java)
	}
}