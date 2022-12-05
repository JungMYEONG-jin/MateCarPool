package com.example.eunboard.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportBoard is a Querydsl query type for ReportBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportBoard extends EntityPathBase<ReportBoard> {

    private static final long serialVersionUID = 42156901L;

    public static final QReportBoard reportBoard = new QReportBoard("reportBoard");

    public final com.example.eunboard.old.domain.entity.QBaseEntity _super = new com.example.eunboard.old.domain.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath reportStudentId = createString("reportStudentId");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final StringPath writerEmail = createString("writerEmail");

    public final StringPath writerStudentId = createString("writerStudentId");

    public QReportBoard(String variable) {
        super(ReportBoard.class, forVariable(variable));
    }

    public QReportBoard(Path<? extends ReportBoard> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportBoard(PathMetadata metadata) {
        super(ReportBoard.class, metadata);
    }

}

