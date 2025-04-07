package com.roniantonius.latihanspringai.dtos;

public record DetailBuku(
        String kategori,
        String buku,
        String tahun,
        String ulasan,
        String penulis,
        String sinopsis
) {
}