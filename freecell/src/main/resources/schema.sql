drop table cards;
create table cards (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    number varchar(32) not null,
    type varchar(32) not null
);