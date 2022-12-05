package com.example.eunboard.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQuestionBoard is a Querydsl query type for QuestionBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestionBoard extends EntityPathBase<QuestionBoard> {

    private static final long serialVersionUID = 1637004083L;

    public static final QQuestionBoard questionBoard = new QQuestionBoard("questionBoard");

    public final com.example.eunboard.old.domain.entity.QBaseEntity _super = new com.example.eunboard.old.domain.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public final StringPath writerEmail = createString("writerEmail");

    public final StringPath writerStudentId = createString("writerStudentId");

    public QQuestionBoard(String variable) {
        super(QuestionBoard.class, forVariable(variable));
    }

    public QQuestionBoard(Path<? extends QuestionBoard> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuestionBoard(PathMetadata metadata) {
        super(QuestionBoard.class, metadata);
    }

}

