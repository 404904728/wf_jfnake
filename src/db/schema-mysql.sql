CREATE TABLE wf_process (
    id                VARCHAR(32) PRIMARY KEY NOT NULL comment '主键ID',
    name              VARCHAR(100) comment '流程名称',
    display_Name      VARCHAR(200) comment '流程显示名称',
    type              VARCHAR(100) comment '流程类型',
    instance_Url      VARCHAR(200) comment '实例url',
    state             TINYINT(1) comment '流程是否可用',
    content           LONGBLOB comment '流程模型定义',
    version           INT(2) comment '版本',
    create_Time       VARCHAR(50) comment '创建时间',
    creator           VARCHAR(50) comment '创建人'
)comment='流程定义表';

CREATE TABLE wf_order (
    id                VARCHAR(32) NOT NULL PRIMARY KEY comment '主键ID',
    parent_Id         VARCHAR(32) comment '父流程ID',
    process_Id        VARCHAR(32) NOT NULL comment '流程定义ID',
    creator           VARCHAR(50) comment '发起人',
    create_Time       VARCHAR(50) NOT NULL comment '发起时间',
    expire_Time       VARCHAR(50) comment '期望完成时间',
    last_Update_Time  VARCHAR(50) comment '上次更新时间',
    last_Updator      VARCHAR(50) comment '上次更新人',
    priority          TINYINT(1) comment '优先级',
    parent_Node_Name  VARCHAR(100) comment '父流程依赖的节点名称',
    order_No          VARCHAR(50) comment '流程实例编号',
    variable          VARCHAR(2000) comment '附属变量json存储',
    version           INT(3) comment '版本'
)comment='流程实例表';

CREATE TABLE wf_task (
    id                VARCHAR(32) NOT NULL PRIMARY KEY comment '主键ID',
    order_Id          VARCHAR(32) NOT NULL comment '流程实例ID',
    task_Name         VARCHAR(100) NOT NULL comment '任务名称',
    display_Name      VARCHAR(200) NOT NULL comment '任务显示名称',
    task_Type         TINYINT(1) NOT NULL comment '任务类型',
    perform_Type      TINYINT(1) comment '参与类型',
    operator          VARCHAR(50) comment '任务处理人',
    create_Time       VARCHAR(50) comment '任务创建时间',
    finish_Time       VARCHAR(50) comment '任务完成时间',
    expire_Time       VARCHAR(50) comment '任务期望完成时间',
    action_Url        VARCHAR(200) comment '任务处理的url',
    parent_Task_Id    VARCHAR(32) comment '父任务ID',
    variable          VARCHAR(2000) comment '附属变量json存储',
    version           TINYINT(1) comment '版本'
)comment='任务表';

CREATE TABLE wf_task_actor (
    task_Id           VARCHAR(32) not null comment '任务ID',
    actor_Id          VARCHAR(50) not null comment '参与者ID'
)comment='任务参与者表';

create table wf_hist_order (
    id                VARCHAR(32) not null primary key comment '主键ID',
    process_Id        VARCHAR(32) not null comment '流程定义ID',
    order_State       TINYINT(1) not null comment '状态',
    creator           VARCHAR(50) comment '发起人',
    create_Time       VARCHAR(50) not null comment '发起时间',
    end_Time          VARCHAR(50) comment '完成时间',
    expire_Time       VARCHAR(50) comment '期望完成时间',
    priority          TINYINT(1) comment '优先级',
    parent_Id         VARCHAR(32) comment '父流程ID',
    order_No          VARCHAR(50) comment '流程实例编号',
    variable          VARCHAR(2000) comment '附属变量json存储'
)comment='历史流程实例表';

create table wf_hist_task (
    id                VARCHAR(32) not null primary key comment '主键ID',
    order_Id          VARCHAR(32) not null comment '流程实例ID',
    task_Name         VARCHAR(100) not null comment '任务名称',
    display_Name      VARCHAR(200) not null comment '任务显示名称',
    task_Type         TINYINT(1) not null comment '任务类型',
    perform_Type      TINYINT(1) comment '参与类型',
    task_State        TINYINT(1) not null comment '任务状态',
    operator          VARCHAR(50) comment '任务处理人',
    create_Time       VARCHAR(50) not null comment '任务创建时间',
    finish_Time       VARCHAR(50) comment '任务完成时间',
    expire_Time       VARCHAR(50) comment '任务期望完成时间',
    action_Url        VARCHAR(200) comment '任务处理url',
    parent_Task_Id    VARCHAR(32) comment '父任务ID',
    variable          VARCHAR(2000) comment '附属变量json存储'
)comment='历史任务表';

create table wf_hist_task_actor (
    task_Id           VARCHAR(32) not null comment '任务ID',
    actor_Id          VARCHAR(50) not null comment '参与者ID'
)comment='历史任务参与者表';

create table wf_surrogate (
    id                VARCHAR(32) PRIMARY KEY NOT NULL COMMENT '主键ID',
    process_Name       VARCHAR(100) COMMENT '流程名称',
    operator          VARCHAR(50) COMMENT '授权人',
    surrogate         VARCHAR(50) COMMENT '代理人',
    odate             VARCHAR(64) COMMENT '操作时间',
    sdate             VARCHAR(64) COMMENT '开始时间',
    edate             VARCHAR(64) COMMENT '结束时间',
    state             TINYINT(1) COMMENT '状态'
)COMMENT='委托代理表';
create index IDX_SURROGATE_OPERATOR on wf_surrogate (operator);

create table wf_cc_order (
    order_Id        varchar(32) COMMENT '流程实例ID',
    actor_Id        varchar(50) COMMENT '参与者ID',
    creator         varchar(50) COMMENT '发起人',
    create_Time     varchar(50) COMMENT '抄送时间',
    finish_Time     varchar(50) COMMENT '完成时间',
    status          TINYINT(1)  COMMENT '状态'
)comment='抄送实例表';
create index IDX_CCORDER_ORDER on wf_cc_order (order_Id);

create index IDX_PROCESS_NAME on wf_process (name);
create index IDX_ORDER_PROCESSID on wf_order (process_Id);
create index IDX_ORDER_NO on wf_order (order_No);
create index IDX_TASK_ORDER on wf_task (order_Id);
create index IDX_TASK_TASKNAME on wf_task (task_Name);
create index IDX_TASK_PARENTTASK on wf_task (parent_Task_Id);
create index IDX_TASKACTOR_TASK on wf_task_actor (task_Id);
create index IDX_HIST_ORDER_PROCESSID on wf_hist_order (process_Id);
create index IDX_HIST_ORDER_NO on wf_hist_order (order_No);
create index IDX_HIST_TASK_ORDER on wf_hist_task (order_Id);
create index IDX_HIST_TASK_TASKNAME on wf_hist_task (task_Name);
create index IDX_HIST_TASK_PARENTTASK on wf_hist_task (parent_Task_Id);
create index IDX_HIST_TASKACTOR_TASK on wf_hist_task_actor (task_Id);

alter table wf_task_actor
  add constraint FK_TASK_ACTOR_TASKID foreign key (task_Id)
  references wf_task (id);
alter table wf_task
  add constraint FK_TASK_ORDERID foreign key (order_Id)
  references wf_order (id);
alter table wf_order
  add constraint FK_ORDER_PARENTID foreign key (parent_Id)
  references wf_order (id);
alter table wf_order
  add constraint FK_ORDER_PROCESSID foreign key (process_Id)
  references wf_process (id);
alter table wf_hist_task_actor
  add constraint FK_HIST_TASKACTOR foreign key (task_Id)
  references wf_hist_task (id);
alter table wf_hist_task
  add constraint FK_HIST_TASK_ORDERID foreign key (order_Id)
  references wf_hist_order (id);
alter table wf_hist_order
  add constraint FK_HIST_ORDER_PARENTID foreign key (parent_Id)
  references wf_hist_order (id);
alter table wf_hist_order
  add constraint FK_HIST_ORDER_PROCESSID foreign key (process_Id)
  references wf_process (id);
  
  
CREATE TABLE sec_menu (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  description VARCHAR(500) DEFAULT NULL,
  name VARCHAR(200) NOT NULL,
  parent_menu INT DEFAULT NULL,
  orderby INT DEFAULT 0
);

CREATE TABLE sec_resource (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL,
  source VARCHAR(200) DEFAULT NULL,
  menu INT DEFAULT NULL
);
ALTER TABLE SEC_RESOURCE ADD UNIQUE (NAME);
ALTER TABLE SEC_RESOURCE ADD CONSTRAINT FK_RESOURCE_MENU FOREIGN KEY (MENU) REFERENCES SEC_MENU (ID);

CREATE TABLE sec_authority (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  description VARCHAR(500) DEFAULT NULL,
  name VARCHAR(200) NOT NULL
);
ALTER TABLE SEC_AUTHORITY ADD UNIQUE (NAME);

CREATE TABLE sec_authority_resource (
  authority_id INT NOT NULL,
  resource_id INT NOT NULL
);
ALTER TABLE SEC_AUTHORITY_RESOURCE ADD CONSTRAINT FK_AUTHORITY_RESOURCE1 FOREIGN KEY (AUTHORITY_ID) REFERENCES SEC_AUTHORITY (ID);
ALTER TABLE SEC_AUTHORITY_RESOURCE ADD CONSTRAINT FK_AUTHORITY_RESOURCE2 FOREIGN KEY (RESOURCE_ID) REFERENCES SEC_RESOURCE (ID);

CREATE TABLE sec_role (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  description VARCHAR(500) DEFAULT NULL,
  name VARCHAR(200) NOT NULL
);
ALTER TABLE SEC_ROLE ADD UNIQUE (NAME);

CREATE TABLE sec_role_authority (
  role_id INT NOT NULL,
  authority_id INT NOT NULL
);
ALTER TABLE SEC_ROLE_AUTHORITY ADD CONSTRAINT FK_ROLE_AUTHORITY1 FOREIGN KEY (AUTHORITY_ID) REFERENCES SEC_AUTHORITY (ID);
ALTER TABLE SEC_ROLE_AUTHORITY ADD CONSTRAINT FK_ROLE_AUTHORITY2 FOREIGN KEY (ROLE_ID) REFERENCES SEC_ROLE (ID);

CREATE TABLE sec_org (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  active VARCHAR(255) DEFAULT NULL,
  description VARCHAR(500) DEFAULT NULL,
  fullname VARCHAR(200) DEFAULT NULL,
  name VARCHAR(200) NOT NULL,
  type VARCHAR(200) DEFAULT NULL,
  parent_org INT DEFAULT NULL
);
ALTER TABLE SEC_ORG ADD CONSTRAINT FK_ORG_PARENT FOREIGN KEY (PARENT_ORG) REFERENCES SEC_ORG (ID);

CREATE TABLE sec_user (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  address VARCHAR(200) DEFAULT NULL,
  age INT(11) DEFAULT NULL,
  email VARCHAR(100) DEFAULT NULL,
  enabled VARCHAR(255) DEFAULT NULL,
  fullname VARCHAR(100) DEFAULT NULL,
  password VARCHAR(50) DEFAULT NULL,
  plainPassword VARCHAR(50) DEFAULT NULL,
  salt VARCHAR(255) DEFAULT NULL,
  sex VARCHAR(255) DEFAULT NULL,
  type INT(11) DEFAULT NULL,
  username VARCHAR(50) NOT NULL,
  org INT DEFAULT NULL
);
ALTER TABLE SEC_USER ADD UNIQUE (USERNAME);
ALTER TABLE SEC_USER ADD CONSTRAINT FK_USER_ORG FOREIGN KEY (ORG) REFERENCES SEC_ORG (ID);

CREATE TABLE sec_role_user (
  user_id INT NOT NULL,
  role_id INT NOT NULL
);
ALTER TABLE SEC_ROLE_USER ADD CONSTRAINT FK_ROLE_USER1 FOREIGN KEY (USER_ID) REFERENCES SEC_USER (ID);
ALTER TABLE SEC_ROLE_USER ADD CONSTRAINT FK_ROLE_USER2 FOREIGN KEY (ROLE_ID) REFERENCES SEC_ROLE (ID);

CREATE TABLE sec_user_authority (
  user_id INT NOT NULL,
  authority_id INT NOT NULL
);
ALTER TABLE SEC_USER_AUTHORITY ADD CONSTRAINT FK_USER_AUTHORITY1 FOREIGN KEY (AUTHORITY_ID) REFERENCES SEC_AUTHORITY (ID);
ALTER TABLE SEC_USER_AUTHORITY ADD CONSTRAINT FK_USER_AUTHORITY2 FOREIGN KEY (USER_ID) REFERENCES SEC_USER (ID);

CREATE TABLE conf_dictionary (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  cnName VARCHAR(200) NOT NULL,
  description VARCHAR(500) DEFAULT NULL,
  name VARCHAR(200) NOT NULL
);
ALTER TABLE CONF_DICTIONARY ADD UNIQUE (NAME);

CREATE TABLE conf_dictitem (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(50) DEFAULT NULL,
  description VARCHAR(500) DEFAULT NULL,
  name VARCHAR(200) NOT NULL,
  orderby INT(11) DEFAULT NULL,
  dictionary INT DEFAULT NULL
);
ALTER TABLE CONF_DICTITEM ADD UNIQUE (NAME);
ALTER TABLE CONF_DICTITEM ADD CONSTRAINT FK_DICTITEM_DICTIONARY FOREIGN KEY (DICTIONARY) REFERENCES CONF_DICTIONARY (ID);

CREATE TABLE df_form (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL,
  displayName VARCHAR(200) DEFAULT NULL,
  type VARCHAR(50) DEFAULT NULL,
  creator VARCHAR(50) DEFAULT NULL,
  createTime VARCHAR(50) DEFAULT NULL,
  originalHtml TEXT,
  parseHtml TEXT,
  fieldNum INT DEFAULT 0
);
ALTER TABLE DF_FORM ADD UNIQUE (NAME);

CREATE TABLE df_field (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL,
  plugins VARCHAR(200) DEFAULT NULL,
  title VARCHAR(50) DEFAULT NULL,
  type  VARCHAR(50) DEFAULT NULL,
  flow  VARCHAR(10) DEFAULT NULL,
  tableName VARCHAR(50) DEFAULT NULL,
  formId INT NOT NULL
);

CREATE TABLE flow_approval (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  operator VARCHAR(50) NOT NULL,
  operateTime VARCHAR(50),
  result VARCHAR(50),
  description VARCHAR(500) DEFAULT NULL,
  orderId VARCHAR(50),
  taskId VARCHAR(50),
  taskName VARCHAR(100)
);

CREATE TABLE flow_borrow (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  operator VARCHAR(50) NOT NULL,
  description VARCHAR(500) DEFAULT NULL,
  amount DOUBLE,
  operateTime VARCHAR(50),
  repaymentDate VARCHAR(50),
  orderId VARCHAR(50)
);