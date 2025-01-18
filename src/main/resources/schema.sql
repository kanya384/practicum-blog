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

insert into posts(id, title, image, content, likes) values (-1, 'Алькарас обыграл Боржеша и вышел в четвертый круг Australian Open', 'https://ss.sport-express.ru/userfiles/materials/207/2078824/volga.jpg', 'Испанский теннисист Карлос Алькарас победил португальца Нуну Боржеша в третьем круге Открытого чемпионата Австралии-2025 — 6:2, 6:4, 6:7 (3:7), 6:2.<br /> Игра продолжалась 2 часа 57 минут.<br /> В следующем круге испанец сыграет с победителем пары Джек Дрейпер (Великобритания, 15) - Александр Вукич (Австралия).', 10);
insert into tags(id, title) values (-1, 'теннис');
insert into tags(id, title) values (-2, 'спорт');
insert into post_tags(post_id, tag_id) values (-1, -1);
insert into post_tags(post_id, tag_id) values (-1, -2);
insert into comments(id, post_id, content) values (-1, -1, 'Карлито просто обязан забрать Австралию в этом году');
insert into comments(id, post_id, content) values (-2, -1, 'Синнер заберет, шансов нету');


insert into posts(id, title, image, content, likes) values (-2, '«Манчестер Сити» берет конкурента Холанну. Станет ли Мармуш новой египетской звездой АПЛ?', 'https://ss.sport-express.ru/userfiles/materials/207/2078785/volga.jpg', 'Фабрицио Романо уже подтвердил сделку. Нападающий «Айнтрахта» и сборной Египта Омар Мармуш переходит в «Манчестер Сити».<br /> По информации инсайдера Фабрицио Романо, стороны достигли соглашения о трансфере 25-летнего футболиста. Детали трансфера Мармуш подпишет контракт с «Манчестер Сити» сроком на пять лет. Ранее журналист Sky Sport Флориан Плеттенберг сообщал, что «Айнтрахт» требует за переход египтянина 80 миллионов евро. Английский клуб собирался предложить за Мармуша 70 миллионов, но с бонусами эта сумма могла вырасти до 80 миллионов.<br /> Transfermarkt оценивает форварда в 60 миллионов евро.', 2);
insert into tags(id, title) values (-3, 'футбол');
insert into post_tags(post_id, tag_id) values (-2, -2);
insert into post_tags(post_id, tag_id) values (-2, -3);
insert into comments(id, post_id, content) values (-3, -2, 'Пеп начал грандиозную закупку');
insert into comments(id, post_id, content) values (-4, -2, 'Шейхи выворачивают карманы');