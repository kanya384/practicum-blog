create table if not exists posts(
    id bigserial primary key,
    title varchar not null,
    image varchar not null,
    content varchar not null,
    likes integer not null
);

create table if not exists comments(
    id bigserial primary key,
    post_id bigint not null references posts(id) on delete cascade,
    content varchar not null
);

create table if not exists tags(
    id bigserial primary key,
    title varchar unique not null
);

create table if not exists post_tags (
    post_id bigint references posts(id) on delete cascade,
    tag_id bigint references tags(id) on delete cascade,
    primary key (post_id, tag_id)
);