create table cola_oauth_client
(
  id bigint null,
  created_by bigint null,
  created_date timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
  last_modified_by bigint null,
  last_modified_date timestamp default '0000-00-00 00:00:00' not null,
  access_token_validity_seconds int null,
  client_id varchar(200) null,
  client_secret varchar(255) null,
  refresh_token_validity_seconds int null,
  grant_type varchar(255) not null comment '授权类型',
  resource_ids varchar(255) null comment '资源ID',
  redirect_uri varchar(2000) null comment '跳转URL',
  enable bit default b'1' null comment '是否启用'
)
  engine=InnoDB
;

create index cola_oauth_client_client_id_index
  on cola_oauth_client (client_id)
;

create table cola_oauth_client_scope
(
  id bigint auto_increment comment '主键ID'
    primary key,
  client_id bigint null comment '客户端ID',
  scope varchar(50) not null comment '授权范围',
  auto_approve bit default b'0' null
)
  engine=InnoDB
;

create index cola_oauth_client_scope_client_id_index
  on cola_oauth_client_scope (client_id)
;

create table cola_oauth_resource
(
  id bigint auto_increment comment '主键ID'
    primary key,
  name varchar(255) null comment '名称',
  code varchar(255) not null comment '编号',
  description varchar(200) null comment '备注',
  enable bit default b'1' null,
  constraint cola_sys_service_code_uindex
  unique (code)
)
  comment '服务表' engine=InnoDB
;

