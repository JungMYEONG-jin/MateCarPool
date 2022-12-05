package com.example.eunboard.passenger.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPassenger is a Querydsl query type for Passenger
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPassenger extends EntityPathBase<Passenger> {

    private static final long serialVersionUID = -1606890247L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPassenger passenger = new QPassenger("passenger");

    public final com.example.eunboard.old.domain.entity.QBaseEntity _super = new com.example.eunboard.old.domain.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> isCancel = createNumber("isCancel", Integer.class);

    public final com.example.eunboard.member.domain.QMember member;

    public final com.example.eunboard.ticket.domain.QTicket ticket;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QPassenger(String variable) {
        this(Passenger.class, forVariable(variable), INITS);
    }

    public QPassenger(Path<? extends Passenger> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPassenger(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPassenger(PathMetadata metadata, PathInits inits) {
        this(Passenger.class, metadata, inits);
    }

    public QPassenger(Class<? extends Passenger> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.eunboard.member.domain.QMember(forProperty("member")) : null;
        this.ticket = inits.isInitialized("ticket") ? new com.example.eunboard.ticket.domain.QTicket(forProperty("ticket"), inits.get("ticket")) : null;
    }

}

