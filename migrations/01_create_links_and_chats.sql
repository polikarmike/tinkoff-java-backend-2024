create table Link
(
    id              bigint generated always as identity,
    url             text                     not null,

    created_at      timestamp with time zone not null,
    last_updated_at timestamp with time zone,

    primary key (id),
    unique (url)
);

create table Chat
(
    id              bigint                   not null,

    created_at      timestamp with time zone not null,

    primary key (id)
);

create table Link_Chat
(
    link_id         bigint references Link(id) on delete cascade,
    chat_id         bigint references Chat(id) on delete cascade,

    primary key (link_id, chat_id)
);
