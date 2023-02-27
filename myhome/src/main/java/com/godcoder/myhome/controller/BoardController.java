package com.godcoder.myhome.controller;

import com.godcoder.myhome.model.Board;
import com.godcoder.myhome.repository.BoardRepository;
import com.godcoder.myhome.validator.BoardVaildator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    private BoardRepository boardRepository;
    private BoardVaildator boardVaildator;

    public BoardController(BoardRepository boardRepository, BoardVaildator boardVaildator){
        this.boardRepository = boardRepository;
        this.boardVaildator = boardVaildator;
    }

    @GetMapping("/list")
    public String list(Model model){
        List<Board> boards = boardRepository.findAll();
        model.addAttribute("boards",boards);
        return "board/list";
    }

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long id){
        if(id == null){
            model.addAttribute("board", new Board());
        } else {
            Board board = boardRepository.findById(id).orElse(null);
            model.addAttribute("board",board);
        }

        return "board/form";
    }

    @PostMapping("/form")
    public String formSubmit(@Valid Board board, BindingResult bindingresult){
        boardVaildator.validate(board, bindingresult);

        if(bindingresult.hasErrors()){
            System.out.println("오류 발생");
            return "board/form";
        }
        boardRepository.save(board);
        return "redirect:/board/list";
    }
}
