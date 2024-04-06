package com.pay1oad.homepage.dto;

import com.pay1oad.homepage.model.Post;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Integer postid;

    private String subject;

    private String content;

    private LocalDateTime Date;

    private String Post_type;

    public PostDTO(final Post entity){
        this.postid= entity.getPostid();
        this.subject=entity.getSubject();
        this.content= entity.getContent();
        this.Date=entity.getDate();
        this.Post_type=entity.getPost_type();
    }

}
