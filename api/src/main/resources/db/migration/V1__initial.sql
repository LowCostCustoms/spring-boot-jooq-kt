create table tags(
    id uuid primary key,
    name varchar not null
);

alter table tags add constraint uq__tags__name unique(name);

create table pets(
    id uuid primary key,
    name varchar not null,
    status varchar not null
);

create table pet_tags(
    pet_id uuid,
    tag_id uuid,
    foreign key(pet_id) references pets(id) on delete cascade,
    foreign key(tag_id) references tags(id) on delete cascade
);

alter table pet_tags add constraint uq__pet_tags__pet_id_tag_id unique(pet_id, tag_id);
