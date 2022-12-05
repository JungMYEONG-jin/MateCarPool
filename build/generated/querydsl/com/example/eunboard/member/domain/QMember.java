package com.example.eunboard.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1780992795L;

    public static final QMember member = new QMember("member1");

    public final com.example.eunboard.old.domain.entity.QBaseEntity _super = new com.example.eunboard.old.domain.entity.QBaseEntity(this);

    public final StringPath area = createString("area");

    public final EnumPath<MemberRole> auth = createEnum("auth", MemberRole.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final DateTimePath<java.util.Date> deleteDate = createDateTime("deleteDate", java.util.Date.class);

    public final StringPath department = createString("department");

    public final StringPath email = createString("email");

    public final BooleanPath isMember = createBoolean("isMember");

    public final NumberPath<Integer> isRemoved = createNumber("isRemoved", Integer.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath memberName = createString("memberName");

    public final ListPath<com.example.eunboard.timetable.domain.MemberTimetable, com.example.eunboard.timetable.domain.QMemberTimetable> memberTimeTableList = this.<com.example.eunboard.timetable.domain.MemberTimetable, com.example.eunboard.timetable.domain.QMemberTimetable>createList("memberTimeTableList", com.example.eunboard.timetable.domain.MemberTimetable.class, com.example.eunboard.timetable.domain.QMemberTimetable.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath profileImage = createString("profileImage");

    public final StringPath studentNumber = createString("studentNumber");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

