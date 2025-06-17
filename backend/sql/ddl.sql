-- 创建库
create database if not exists LoveAdoption;

-- 切换库
use LoveAdoption;

create table animal
(
    id              bigint auto_increment comment '动物ID，主键'
        primary key,
    name            varchar(50)                        not null comment '动物的名字',
    species         varchar(50)                        not null comment '动物物种',
    age             int                                null comment '动物年龄',
    gender          int                                not null comment '动物性别(0公，1母）',
    description     text                               null comment '动物的描述',
    health_status   varchar(100)                       null comment '动物健康状况',
    adoption_status int      default 0                 null comment '动物领养状态，0 表示可领养，1 表示已领养 ,2代表已删除',
    image_url       varchar(255)                       null comment '动物图片的URL',
    created_time    datetime default CURRENT_TIMESTAMP null comment '动物记录创建时间',
    updated_time    datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '记录更新时间',
    deleted_time    datetime                           null comment '软删除时间，标记动物记录删除',
    breed           varchar(10)                        not null comment '大的动物品种（狗1，猫2，鸟3，兔4...)',
    userId          bigint                             null comment '发布的用户id'
);

create table application
(
    id                 bigint auto_increment comment '申请ID，主键'
        primary key,
    userId             bigint                             not null comment '申请用户ID，关联user表',
    animalId           bigint                             not null comment '申请领养的动物ID，关联animal表',
    application_reason text                               null comment '用户申请理由（如喜爱动物、有经验等）',
    status             int      default 0                 null comment '申请状态，0：待审核，1：通过，2：拒绝，3：已撤回',
    review_comments    text                               null comment '审核备注（管理员审批意见）',
    reviewed_by        bigint                             null comment '审核管理员ID，关联user表',
    applicationTime    datetime default CURRENT_TIMESTAMP null comment '申请提交时间',
    reviewTime         datetime                           null comment '审核时间',
    createdTime        datetime default CURRENT_TIMESTAMP null comment '记录创建时间',
    updatedTime        datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '记录更新时间',
    deletedTime        datetime                           null comment '软删除时间'
);

create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    userName     varchar(256)                           null comment '用户昵称',
    userAccount  varchar(256)                           not null comment '账号',
    userAvatar   varchar(1024)                          null comment '用户头像',
    gender       tinyint                                null comment '性别',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user / admin',
    userPassword varchar(512)                           not null comment '密码',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    constraint uni_userAccount
        unique (userAccount)
)
    comment '用户';


