package com.example.eunboard.old.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWithdrawal is a Querydsl query type for Withdrawal
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWithdrawal extends EntityPathBase<Withdrawal> {

    private static final long serialVersionUID = 1757164562L;

    public static final QWithdrawal withdrawal = new QWithdrawal("withdrawal");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QWithdrawal(String variable) {
        super(Withdrawal.class, forVariable(variable));
    }

    public QWithdrawal(Path<? extends Withdrawal> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWithdrawal(PathMetadata metadata) {
        super(Withdrawal.class, metadata);
    }

}

