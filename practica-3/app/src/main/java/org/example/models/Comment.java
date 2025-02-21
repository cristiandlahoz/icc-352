package org.example.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commentId;
	private String comment;
	private String username;
	private Date date = new Date();

	@ManyToOne
	@JoinColumn(name = "article_id", nullable = false)
	private Article article;
}
