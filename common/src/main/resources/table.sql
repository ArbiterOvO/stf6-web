create table user
(
    id       int unsigned auto_increment comment 'ID'
        primary key,
    username varchar(50)  null comment '用户名',
    password varchar(50)  null comment '密码',
    email    varchar(50)  null comment '邮箱',
    head_img varchar(255) null comment '头像图片',
    is_admin int          null comment '0 普通用户 1管理员'
);

create table tag
(
    id       int unsigned auto_increment comment 'ID'
        primary key,
    name     varchar(50)          null comment '标签名',
    tag_type tinyint(1) default 0 null comment '标签列表 0热门标签 1角色标签'
);

create table post_tag
(
    post_id bigint not null,
    tag_id  int    not null
);

create table post_tag
(
    post_id bigint not null,
    tag_id  int    not null
);

create table post_img
(
    post_id int          not null,
    img     varchar(255) not null
);

create table post
(
    id          int unsigned auto_increment comment 'ID'
        primary key,
    title       varchar(50)          null comment '帖子名',
    content     longtext             null comment '帖子内容',
    author_id   int                  null comment '发布人id',
    like_num    int        default 0 null comment '点赞数',
    view_num    int        default 0 null comment '访问数',
    create_time datetime             null comment '发布日期',
    update_time datetime             null comment '更新时间',
    is_checked  tinyint(1) default 0 null comment '是否已审核：0未审核，1已审核'
);

create table comment_like
(
    comment_id   int        not null,
    user_id      int        not null,
    liked_status tinyint(1) not null comment '1点赞 0没有点赞',
    create_time  datetime   null comment '发布日期',
    update_time  datetime   null comment '更新时间'
);

create table comment_comment
(
    id          int unsigned auto_increment comment 'ID'
        primary key,
    content     text          not null comment '内容',
    user_id     int           not null comment '用户id',
    comment_id  int           not null comment '评论id',
    create_time datetime      null comment '发布时间',
    like_num    int default 0 null
);

create table comment2_like
(
    comment2_id  int        not null,
    user_id      int        not null,
    liked_status tinyint(1) not null comment '1点赞 0没有点赞',
    create_time  datetime   null comment '发布日期',
    update_time  datetime   null comment '更新时间'
);

create table comment
(
    id          int unsigned auto_increment comment 'ID'
        primary key,
    content     text          not null comment '内容',
    post_id     int           not null comment '评论表id',
    user_id     int           not null comment '用户id',
    create_time datetime      null comment '发布时间',
    like_num    int default 0 null comment '点赞数'
);

create table post_like
(
    post_id      int        not null,
    user_id      int        not null,
    liked_status tinyint(1) not null comment '1点赞 0没有点赞',
    create_time  datetime   null comment '发布日期',
    update_time  datetime   null comment '更新时间'
);
