package com.example.eunboard.domain.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "report_board")
@ToString
public class ReportBoard extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_board_id")
    private Long id;

    /** 멤버 */
    @Column(name = "member_id")
    private Long memberId;

    /** 신고한 학번 */
    @Column(name = "writer_student_id")
    private String writerStudentId;

    /** 신고한 email */
    @Column(name = "writer_email")
    private String writerEmail;

    /** 신고받은 학번 */
    @Column(name = "report_student_id")
    private String reportStudentId;

    /** 신고 내용 */
    @Column(name = "content")
    private String content;

}
