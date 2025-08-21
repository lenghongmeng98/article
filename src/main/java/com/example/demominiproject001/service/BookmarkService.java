package com.example.demominiproject001.service;

import com.example.demominiproject001.model.entity.Bookmark;
import com.example.demominiproject001.model.response.ArticleDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BookmarkService {

    List<ArticleDTO> getAllBookmarkArticles(int page, int size, Sort.Direction sortDirection);

    ArticleDTO addBookmark(Long articleId);

    void deleteBookmark(Long articleId);
}
