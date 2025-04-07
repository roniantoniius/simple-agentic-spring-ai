package com.roniantonius.latihanspringai.controllers;

import com.roniantonius.latihanspringai.dtos.DetailBuku;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gpt-chat")
public class ChatController {
    private final ChatClient chatClient;

    // initialize chat client session
    public ChatController(ChatClient.Builder chatClient){
        this.chatClient = chatClient.build();
    }

    @PostMapping(path = "/v1/tanya-film")
    public String getResponse(
            @RequestParam String kategori, String tahun
    ){
        return chatClient.prompt()
                .user(teks ->
                        teks.text("Tolong buatkan detail dari buku untuk kategori {kategori} dan tahun {tahun} dalam format JSON")
                                .param("kategori", kategori)
                                .param("tahun", tahun)
                ).call().content();
    }

    // method using BeanOutputConverter untuk memastikan output berupa json dengan mendefinisikan sebagai entitas data
    @PostMapping(path = "/v2/tanya-film")
    public DetailBuku getBeanResponse(
            @RequestParam String kategori, String tahun
    ){
        return chatClient.prompt()
                .user(teks ->
                        teks.text("Tolong buatkan detail dari buku untuk kategori {kategori} dan tahun {tahun}.")
                                .param("kategori", kategori)
                                .param("tahun", tahun)
                ).call().entity(DetailBuku.class);
    }

    // this is what ive been loking for. An ParameterizedTypeReference to display several output
    @PostMapping(path = "/v3/tanya-film")
    public List<DetailBuku> getListBeanResponse(
            @RequestParam String kategori, String tahun
    ){
        return chatClient.prompt()
                .user(t ->
                        t.text("Tolong buatkan 2 detail dari buku dengan kategori {kategori} dan tahun {tahun}")
                                .param("kategori", kategori)
                                .param("tahun", tahun))
                .call()
                .entity(new ParameterizedTypeReference<List<DetailBuku>>() {
                });
    }

    // output as a list of String using ListOutputConverter
    @PostMapping(path = "/v4/tanya-film")
    public List<String> getListResponse(
            @RequestParam String kategori, String tahun
    ){
        return chatClient.prompt()
                .user(t ->
                        t.text("Tolong buatkan minimal 5 detail dari buku dengan kategori {kategori} dan tahun {tahun}")
                                .param("kategori", kategori)
                                .param("tahun", tahun))
                .call()
                .entity(new ListOutputConverter(new DefaultConversionService())); // useful of generic list
    }

    // method untuk mengembalikan result dalam bentuk Map yang generic
    @PostMapping(path = "/v5/tanya-film")
    public Map<String, Object> getMapResponse(
            @RequestParam String kategori, String tahun
    ){
        return chatClient.prompt()
                .user(t ->
                        t.text("Tolong buatkan beberapa detail dari buku dengan kategori {kategori} dan tahun {tahun}. \n" +
                                        "Selain itu cantumkan sinopsis dari bukunya, sehingga informasi yang ditampilkan harus \n" +
                                        "dibatasi dan tidak terlalu dalam. Respons yang kamu berikan harus mengandung informasi \n" +
                                        "kategori, buku, tahun, ulasan, penulis, sinopsis.")
                                .param("kategori", kategori)
                                .param("tahun", tahun))
                .call()
                .entity(new MapOutputConverter()); // useful of generic list
    }
}
