package com.taotao.search.web.controller;

import com.taotao.common.TaoResult;
import com.taotao.manager.domain.Item;
import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

/**
 * Created by 杨清华.
 * on 2017/11/14.
 */
@Controller
@RequestMapping("/search")
public class SearchController {

    @Value("${SEARCH_TAOTAO_ITEM_ROWS}")
    private Integer rows;//搜索结果的显示数量

    @Autowired
    private SearchService searchService;

    /**
     * 搜索
     * @param keyword
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String search(@RequestParam("q") String keyword, @RequestParam(defaultValue = "1") Integer page, Model model) {
        //解决乱码
        try {
            keyword = new String(keyword.getBytes("ISO8859-1"), "UTF-8");

            //查询
            TaoResult<Item> taoResult = searchService.search(keyword, page, rows);

            model.addAttribute("query", keyword);//查询关键字
            model.addAttribute("itemList", taoResult.getRows());//查询结果集合
            model.addAttribute("page", page);//当前页
            model.addAttribute("totalPages", (taoResult.getTotal() + rows -1) / rows);//总页数=(记录总条数+每页显示数量-1) / 每页显示数量

            return "search";//前往search，展示查询结果
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
