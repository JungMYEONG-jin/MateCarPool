package com.example.eunboard.domain.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "question_board")
@ToString
public class QuestionBoard extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_board_id")
    private Long id;

    /** 멤버 */
    @Column(name = "member_id")
    private Long memberId;

    /** 문의학번 */
    @Column(name = "writer_student_id")
    private String writerStudentId;

    /** 문의 email */
    @Column(name = "writer_email")
    private String writerEmail;

    /** 제목 */
    @Column(name = "title")
    private String title;

    /** 내용 */
    @Column(name = "content")
    private String content;


}
