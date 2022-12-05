package com.example.eunboard.timetable.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberTimetable is a Querydsl query type for MemberTimetable
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberTimetable extends EntityPathBase<MemberTimetable> {

    private static final long serialVersionUID = 2015659103L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberTimetable memberTimetable = new QMemberTimetable("memberTimetable");

    public final StringPath dayCode = createString("dayCode");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.eunboard.member.domain.QMember member;

    public QMemberTimetable(String variable) {
        this(MemberTimetable.class, forVariable(variable), INITS);
    }

    public QMemberTimetable(Path<? extends MemberTimetable> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberTimetable(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberTimetable(PathMetadata metadata, PathInits inits) {
        this(MemberTimetable.class, metadata, inits);
    }

    public QMemberTimetable(Class<? extends MemberTimetable> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.eunboard.member.domain.QMember(forProperty("member")) : null;
    }

}

