package com.zcf.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.zcf.dto.User;
import com.zcf.dto.UserQueryCondition;
import com.zcf.exception.UserNotExistException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * 获取认证用户信息
     * @return
     */
    @GetMapping("/me")
    public Object getCurrentUser(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable String id){
        System.out.println(id);

    }
    @PutMapping("/{id:\\d+}")
    @SuppressWarnings("all")
    public User update(@Valid @RequestBody User user, BindingResult erros) {
        if (erros.hasErrors()) {
            erros.getAllErrors().stream().forEach(error -> {
                        FieldError fieldError = (FieldError) error;
                        String message = fieldError.getField() +"  "+ error.getDefaultMessage();
                        System.out.println(message);

                    }
            );
        }
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getId());
        System.out.println(user.getBirthday());
        user.setId(1);
        return user;
    }

    @JsonView(User.UserSimpleView.class)
    @PostMapping
    /**
     * 使用@Valid注解标注需要验证的字段，和BindingResult类获取错误信息
     */
    public User create(@Valid @RequestBody User user) {
        /*//当验证字段发生错误时
        if (erros.hasErrors()) {
            //这里是拿到所有的错误遍历错误，打印错误信息。在开发中就可以通过抛出异常将错误返回给前端
            erros.getAllErrors().stream().forEach(error -> System.out.println(error.getDefaultMessage()));
        }*/
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getId());
        System.out.println(user.getBirthday());
        user.setId(1);
        return user;
    }

    @GetMapping
    @JsonView(User.UserSimpleView.class)
    @ApiOperation(value = "用户查询服务")
    //使用UserQueryCondition接受前端传来的数据，并且使用@PageableDefault注解设置默认分页
    public List<User> query(UserQueryCondition userQueryCondition, @PageableDefault(page = 1, size = 10, sort = "username.asc") Pageable pageable) {
        System.out.println(ReflectionToStringBuilder.toString(userQueryCondition, ToStringStyle.MULTI_LINE_STYLE));
        //System.out.println(userQueryCondition);
        System.out.println(pageable.getPageSize());
        System.out.println(pageable.getPageNumber());
        System.out.println(pageable.getSort());

        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());
        return users;
    }

    @GetMapping("/{id:\\d+}")
    @JsonView(User.UserDetailView.class)
    public User getInfo(@ApiParam(value = "用户id") @PathVariable(value = "id") String id) {
        //throw new UserNotExistException(id);
        //throw new RuntimeException();
        System.out.println("进入getInfo服务");
        User user = new User();
        user.setUsername("zcf");
        return user;
    }
}


