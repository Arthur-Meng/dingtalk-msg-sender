# dingtalk-msg-sender

#### Background

Nail group supports adding custom robots. We can push messages to the nail group in the form of webhook: that is, we can call the given webhook address and send the corresponding request to push personalized messages.

#### Introduction

The current project is a scaffold project that pushes messages to the nail group and supports configuration in the form of tasks. The second development is very simple and can be used out of the box.

#### Software function

The current project is built based on springboot, with built-in nailing SDK and tool class for sending nailing group messages, which can easily send messages.

The current project also introduces quartz as a task scheduling framework, which provides a test task for sending messages. We can customize the sending task in the form of configuration.

Of course, you can also choose secondary development, customize your own tasks, and imitate the test tasks.

#### How to add a custom robot

1. Select the group settings of the nail group

2. Click intelligent swarm assistant - add robot

3. Select the customized robot (accessing the customized service through webhook)

4. Configure the name of the robot

5. Check and sign (record the secret key, which needs to be configured later)

6. Finally, click finish, and then we can get the webhook address (record this address, which needs to be configured later)

#### Test task instructions

1. Configure application.properties

* The test task is prefixed with test.Job[0], and multiple configurations are supported. The number in [] is the
 task serial number

* Test.Job[0].Title indicates the title of the first test task

* Test.Job[0].Content represents the content of the first test task, with multiple lines separated by commas

* Test.Job[0].Cron represents the cron expression of the first test task (please check the cron table expression
 yourself if you don't understand)

* Test.Job[0].Atuserphone indicates the mobile phone number of the person who needs @ in the first test task
 notification group, and multiple numbers are separated by commas

* Test.Job[0].Webhook represents the webhook address of the swarm robot of the first test task

* Test.Job[0].Robot secret represents the secret key of the swarm robot of the first test task

2. Start the project to see if the message will be pushed to the nail group

#### Participation and contribution

1. [Meng Jiawei]（ https://github.com/Arthur-Meng )