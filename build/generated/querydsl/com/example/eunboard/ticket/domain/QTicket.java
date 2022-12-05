package com.example.eunboard.ticket.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTicket is a Querydsl query type for Ticket
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTicket extends EntityPathBase<Ticket> {

    private static final long serialVersionUID = 168904265L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTicket ticket = new QTicket("ticket");

    public final com.example.eunboard.old.domain.entity.QBaseEntity _super = new com.example.eunboard.old.domain.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath endArea = createString("endArea");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath kakaoOpenChatTitle = createString("kakaoOpenChatTitle");

    public final StringPath kakaoOpenChatUrl = createString("kakaoOpenChatUrl");

    public final com.example.eunboard.member.domain.QMember member;

    public final ListPath<com.example.eunboard.passenger.domain.Passenger, com.example.eunboard.passenger.domain.QPassenger> passengerList = this.<com.example.eunboard.passenger.domain.Passenger, com.example.eunboard.passenger.domain.QPassenger>createList("passengerList", com.example.eunboard.passenger.domain.Passenger.class, com.example.eunboard.passenger.domain.QPassenger.class, PathInits.DIRECT2);

    public final NumberPath<Integer> recruitPerson = createNumber("recruitPerson", Integer.class);

    public final StringPath startArea = createString("startArea");

    public final StringPath startDtime = createString("startDtime");

    public final EnumPath<TicketStatus> status = createEnum("status", TicketStatus.class);

    public final StringPath ticketPrice = createString("ticketPrice");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QTicket(String variable) {
        this(Ticket.class, forVariable(variable), INITS);
    }

    public QTicket(Path<? extends Ticket> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTicket(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTicket(PathMetadata metadata, PathInits inits) {
        this(Ticket.class, metadata, inits);
    }

    public QTicket(Class<? extends Ticket> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.eunboard.member.domain.QMember(forProperty("member")) : null;
    }

}

