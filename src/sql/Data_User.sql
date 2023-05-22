INSERT INTO [User](Username, [Password], [Name], Email, DateCreated, DateModified, IsEnabled, UserType)
VALUES('admin', 'admin', 'Administrator', 'a@gmail.com', DATEDIFF_BIG(ms, '1970-01-01 00:00:00', GetUtcDate()), DATEDIFF_BIG(ms, '1970-01-01 00:00:00', GetUtcDate()), 1, 2)

INSERT INTO [User](Username, [Password], [Name], Email, DateCreated, DateModified, IsEnabled, UserType)
VALUES('staff', 'staff', 'Staff', 's@gmail.com', DATEDIFF_BIG(ms, '1970-01-01 00:00:00', GetUtcDate()), DATEDIFF_BIG(ms, '1970-01-01 00:00:00', GetUtcDate()), 1, 1)

INSERT INTO [User](Username, [Password], [Name], Email, DateCreated, DateModified, IsEnabled, UserType)
VALUES('user1', 'user1', 'User 1', 'u1@gmail.com', DATEDIFF_BIG(ms, '1970-01-01 00:00:00', GetUtcDate()), DATEDIFF_BIG(ms, '1970-01-01 00:00:00', GetUtcDate()), 1, 0)

INSERT INTO [User](Username, [Password], [Name], Email, DateCreated, DateModified, IsEnabled, DisabledReason, UserType)
VALUES('user2', 'user2', 'User 2', 'u2@gmail.com', DATEDIFF_BIG(ms, '1970-01-01 00:00:00', GetUtcDate()), DATEDIFF_BIG(ms, '1970-01-01 00:00:00', GetUtcDate()), 0, NULL, 0)

INSERT INTO [User](Username, [Password], [Name], Email, DateCreated, DateModified, IsEnabled, DisabledReason, UserType)
VALUES('user3', 'user3', 'User 3', 'u3@gmail.com', DATEDIFF_BIG(ms, '1970-01-01 00:00:00', GetUtcDate()), DATEDIFF_BIG(ms, '1970-01-01 00:00:00', GetUtcDate()), 0, N'123456', 0)

GO