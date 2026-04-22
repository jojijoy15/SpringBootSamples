-- ============================================================
--  SEED DATA — Employee Management System
--  Target  : MySQL 8.0+
--  Schema  : Spring Boot 3 / Hibernate 6 auto-generated DDL
--  Note    : Converted from PostgreSQL seed_data.sql
--
--  Tables  : DEPARTMENT (10), EMPLOYEE (105), PROJECT (20),
--            EMP_PROJECT (135), SALARY_HISTORY (110),
--            AUDIT_LOG (110)
--
--  Insert order respects FK constraints.
--  The Employee self-FK (manager_id) is handled by loading
--  directors first, then managers, then individual contributors.
-- ============================================================

START TRANSACTION;

-- ─────────────────────────────────────────────────────────────
-- Optional cleanup — uncomment on a fresh / dev database only
-- ─────────────────────────────────────────────────────────────
-- TRUNCATE TABLE audit_log, salary_history, emp_project,
--               employee, project, department;

-- ═════════════════════════════════════════════════════════════
-- 1. DEPARTMENT  (10 rows)
-- ═════════════════════════════════════════════════════════════
INSERT INTO department (dept_id, dept_name, budget) VALUES
( 1, 'Engineering',         5000000.00),
( 2, 'Human Resources',     1500000.00),
( 3, 'Finance',             2000000.00),
( 4, 'Marketing',           1800000.00),
( 5, 'Operations',          2500000.00),
( 6, 'Sales',               3000000.00),
( 7, 'Legal',               1200000.00),
( 8, 'Product Management',  2200000.00),
( 9, 'Data Science',        3500000.00),
(10, 'Customer Support',    1000000.00);

-- ═════════════════════════════════════════════════════════════
-- 2. EMPLOYEE  (105 rows in 3 batches — respects self-FK)
-- ═════════════════════════════════════════════════════════════

-- ── Batch A: Directors / VP level  (manager_id = NULL) ───────
INSERT INTO employee
    (emp_id, emp_name, email, salary, join_date, location, version, dept_id, manager_id)
VALUES
( 1, 'Alice Johnson',  'alice.johnson@corp.com',   150000.00, '2015-03-01', 'New York',       0,  1, NULL),
( 2, 'Bob Martinez',   'bob.martinez@corp.com',    145000.00, '2014-07-15', 'Chicago',        0,  2, NULL),
( 3, 'Carol White',    'carol.white@corp.com',     148000.00, '2013-11-20', 'San Francisco',  0,  3, NULL),
( 4, 'David Kim',      'david.kim@corp.com',       142000.00, '2016-01-10', 'New York',       0,  4, NULL),
( 5, 'Emma Davis',     'emma.davis@corp.com',      140000.00, '2015-06-25', 'Austin',         0,  5, NULL),
( 6, 'Frank Wilson',   'frank.wilson@corp.com',    155000.00, '2012-09-05', 'Seattle',        0,  6, NULL),
( 7, 'Grace Lee',      'grace.lee@corp.com',       138000.00, '2017-04-18', 'Boston',         0,  7, NULL),
( 8, 'Henry Brown',    'henry.brown@corp.com',     143000.00, '2016-08-30', 'Denver',         0,  8, NULL),
( 9, 'Iris Chen',      'iris.chen@corp.com',       160000.00, '2014-02-14', 'San Francisco',  0,  9, NULL),
(10, 'Jack Taylor',    'jack.taylor@corp.com',     130000.00, '2018-05-22', 'Phoenix',        0, 10, NULL);

-- ── Batch B: Senior Managers  (manager_id refs emp 1–10) ─────
INSERT INTO employee
    (emp_id, emp_name, email, salary, join_date, location, version, dept_id, manager_id)
VALUES
(11, 'Karen Anderson',  'karen.anderson@corp.com',  110000.00, '2017-03-14', 'New York',       0,  1,  1),
(12, 'Leo Thompson',    'leo.thompson@corp.com',    108000.00, '2018-07-20', 'Chicago',        0,  1,  1),
(13, 'Mia Roberts',     'mia.roberts@corp.com',     105000.00, '2019-01-08', 'New York',       0,  2,  2),
(14, 'Noah Harris',     'noah.harris@corp.com',     107000.00, '2018-11-12', 'Chicago',        0,  2,  2),
(15, 'Olivia Scott',    'olivia.scott@corp.com',    112000.00, '2017-06-30', 'San Francisco',  0,  3,  3),
(16, 'Peter Evans',     'peter.evans@corp.com',     109000.00, '2018-02-14', 'San Francisco',  0,  3,  3),
(17, 'Quinn Foster',    'quinn.foster@corp.com',    106000.00, '2019-03-25', 'New York',       0,  4,  4),
(18, 'Rachel Green',    'rachel.green@corp.com',    104000.00, '2020-01-05', 'Austin',         0,  4,  4),
(19, 'Sam Lewis',       'sam.lewis@corp.com',       111000.00, '2017-09-18', 'Austin',         0,  5,  5),
(20, 'Tina Walker',     'tina.walker@corp.com',     103000.00, '2019-07-22', 'Seattle',        0,  5,  5),
(21, 'Umar Hall',       'umar.hall@corp.com',       115000.00, '2016-12-10', 'Seattle',        0,  6,  6),
(22, 'Vera Young',      'vera.young@corp.com',      113000.00, '2017-05-16', 'Seattle',        0,  6,  6),
(23, 'Walter King',     'walter.king@corp.com',     108000.00, '2018-04-02', 'Boston',         0,  7,  7),
(24, 'Xena Wright',     'xena.wright@corp.com',     106000.00, '2019-09-11', 'Boston',         0,  7,  7),
(25, 'Yusuf Adams',     'yusuf.adams@corp.com',     112000.00, '2017-11-28', 'Denver',         0,  8,  8),
(26, 'Zoe Baker',       'zoe.baker@corp.com',       110000.00, '2018-06-14', 'Denver',         0,  8,  8),
(27, 'Aaron Carter',    'aaron.carter@corp.com',    120000.00, '2016-08-07', 'San Francisco',  0,  9,  9),
(28, 'Beth Collins',    'beth.collins@corp.com',    118000.00, '2017-10-19', 'San Francisco',  0,  9,  9),
(29, 'Chris Nelson',    'chris.nelson@corp.com',    100000.00, '2019-04-30', 'Phoenix',        0, 10, 10),
(30, 'Diana Mitchell',  'diana.mitchell@corp.com',   98000.00, '2020-02-17', 'Phoenix',        0, 10, 10);

-- ── Batch C: Individual Contributors  (manager_id refs emp 11–30) ──
INSERT INTO employee
    (emp_id, emp_name, email, salary, join_date, location, version, dept_id, manager_id)
VALUES
-- Engineering (dept 1)
( 31, 'Ethan Parker',      'ethan.parker@corp.com',       85000.00, '2020-06-01', 'New York',       0,  1, 11),
( 32, 'Fiona Campbell',    'fiona.campbell@corp.com',     82000.00, '2021-01-15', 'New York',       0,  1, 11),
( 33, 'George Reed',       'george.reed@corp.com',        88000.00, '2020-09-20', 'New York',       0,  1, 11),
( 34, 'Hannah Morgan',     'hannah.morgan@corp.com',      84000.00, '2021-03-10', 'New York',       0,  1, 12),
( 35, 'Ian Bell',          'ian.bell@corp.com',           86000.00, '2021-07-05', 'Chicago',        0,  1, 12),
( 36, 'Julia Cook',        'julia.cook@corp.com',         81000.00, '2022-01-20', 'Chicago',        0,  1, 12),
( 37, 'Kevin Murphy',      'kevin.murphy@corp.com',       87000.00, '2020-11-11', 'New York',       0,  1, 11),
( 38, 'Laura Rivera',      'laura.rivera@corp.com',       83000.00, '2021-05-28', 'Chicago',        0,  1, 12),
( 99, 'Uma Dawson',        'uma.dawson@corp.com',         89000.00, '2021-02-14', 'New York',       0,  1, 11),
(100, 'Vince Bradley',     'vince.bradley@corp.com',      91000.00, '2020-05-27', 'New York',       0,  1, 12),
-- Human Resources (dept 2)
( 39, 'Mike Bailey',       'mike.bailey@corp.com',        72000.00, '2020-08-03', 'Chicago',        0,  2, 13),
( 40, 'Nancy Cooper',      'nancy.cooper@corp.com',       70000.00, '2021-02-22', 'Chicago',        0,  2, 13),
( 41, 'Oscar Richardson',  'oscar.richardson@corp.com',   74000.00, '2020-10-14', 'Chicago',        0,  2, 13),
( 42, 'Paula Cox',         'paula.cox@corp.com',          71000.00, '2021-06-07', 'Chicago',        0,  2, 14),
( 43, 'Quinton Ward',      'quinton.ward@corp.com',       73000.00, '2021-09-18', 'Chicago',        0,  2, 14),
( 44, 'Rosa Peterson',     'rosa.peterson@corp.com',      69000.00, '2022-03-01', 'Chicago',        0,  2, 14),
-- Finance (dept 3)
( 45, 'Steve Howard',      'steve.howard@corp.com',       90000.00, '2019-12-01', 'San Francisco',  0,  3, 15),
( 46, 'Tracy Graham',      'tracy.graham@corp.com',       88000.00, '2020-04-15', 'San Francisco',  0,  3, 15),
( 47, 'Uma Sullivan',      'uma.sullivan@corp.com',       92000.00, '2019-08-20', 'San Francisco',  0,  3, 15),
( 48, 'Victor Jenkins',    'victor.jenkins@corp.com',     89000.00, '2020-07-10', 'San Francisco',  0,  3, 16),
( 49, 'Wendy Sanchez',     'wendy.sanchez@corp.com',      91000.00, '2021-01-25', 'San Francisco',  0,  3, 16),
( 50, 'Xavier Wood',       'xavier.wood@corp.com',        87000.00, '2021-05-12', 'San Francisco',  0,  3, 16),
-- Marketing (dept 4)
( 51, 'Yasmine Torres',    'yasmine.torres@corp.com',     78000.00, '2020-09-08', 'New York',       0,  4, 17),
( 52, 'Zachary Phillips',  'zachary.phillips@corp.com',   76000.00, '2021-02-14', 'New York',       0,  4, 17),
( 53, 'Amy Butler',        'amy.butler@corp.com',         80000.00, '2020-11-30', 'Austin',         0,  4, 17),
( 54, 'Brian Simmons',     'brian.simmons@corp.com',      77000.00, '2021-04-19', 'Austin',         0,  4, 18),
( 55, 'Cara Foster',       'cara.foster@corp.com',        79000.00, '2021-08-05', 'Austin',         0,  4, 18),
( 56, 'Derek Price',       'derek.price@corp.com',        75000.00, '2022-01-10', 'New York',       0,  4, 18),
(101, 'Wanda Griffith',    'wanda.griffith@corp.com',     78000.00, '2021-08-09', 'Austin',         0,  4, 17),
-- Operations (dept 5)
( 57, 'Elena Barnes',      'elena.barnes@corp.com',       68000.00, '2020-07-14', 'Austin',         0,  5, 19),
( 58, 'Fred Ross',         'fred.ross@corp.com',          66000.00, '2021-01-08', 'Austin',         0,  5, 19),
( 59, 'Gina Henderson',    'gina.henderson@corp.com',     70000.00, '2020-10-22', 'Austin',         0,  5, 19),
( 60, 'Hank Coleman',      'hank.coleman@corp.com',       67000.00, '2021-03-16', 'Seattle',        0,  5, 20),
( 61, 'Iris Jenkins',      'iris.jenkins@corp.com',       69000.00, '2021-07-29', 'Seattle',        0,  5, 20),
( 62, 'Jake Perry',        'jake.perry@corp.com',         65000.00, '2022-02-04', 'Seattle',        0,  5, 20),
-- Sales (dept 6)
( 63, 'Kim Powell',        'kim.powell@corp.com',         95000.00, '2019-11-11', 'Seattle',        0,  6, 21),
( 64, 'Liam Long',         'liam.long@corp.com',          93000.00, '2020-03-25', 'Seattle',        0,  6, 21),
( 65, 'Molly Patterson',   'molly.patterson@corp.com',    97000.00, '2019-07-08', 'Seattle',        0,  6, 21),
( 66, 'Nate Hughes',       'nate.hughes@corp.com',        94000.00, '2020-06-17', 'Seattle',        0,  6, 22),
( 67, 'Opal Flores',       'opal.flores@corp.com',        96000.00, '2020-09-30', 'Seattle',        0,  6, 22),
( 68, 'Paul Washington',   'paul.washington@corp.com',    92000.00, '2021-01-14', 'Seattle',        0,  6, 22),
( 69, 'Quinn Butler',      'quinn.butler@corp.com',       98000.00, '2019-05-20', 'Seattle',        0,  6, 21),
( 70, 'Rose Diaz',         'rose.diaz@corp.com',          90000.00, '2020-08-12', 'Seattle',        0,  6, 22),
(102, 'Xander Fletcher',   'xander.fletcher@corp.com',    93000.00, '2020-11-22', 'Seattle',        0,  6, 21),
-- Legal (dept 7)
( 71, 'Sean Bryant',       'sean.bryant@corp.com',        95000.00, '2019-10-05', 'Boston',         0,  7, 23),
( 72, 'Tara Russell',      'tara.russell@corp.com',       93000.00, '2020-02-18', 'Boston',         0,  7, 23),
( 73, 'Uriel Griffin',     'uriel.griffin@corp.com',      97000.00, '2019-06-11', 'Boston',         0,  7, 23),
( 74, 'Violet Diaz',       'violet.diaz@corp.com',        94000.00, '2020-05-24', 'Boston',         0,  7, 24),
( 75, 'Wade Hayes',        'wade.hayes@corp.com',         96000.00, '2020-09-08', 'Boston',         0,  7, 24),
( 76, 'Xia Myers',         'xia.myers@corp.com',          92000.00, '2021-01-21', 'Boston',         0,  7, 24),
-- Product Management (dept 8)
( 77, 'Yvette Ford',       'yvette.ford@corp.com',       100000.00, '2019-08-14', 'Denver',         0,  8, 25),
( 78, 'Zack Hamilton',     'zack.hamilton@corp.com',      98000.00, '2020-01-27', 'Denver',         0,  8, 25),
( 79, 'Alice Graham',      'alice.graham@corp.com',      102000.00, '2019-04-09', 'Denver',         0,  8, 25),
( 80, 'Ben Webb',          'ben.webb@corp.com',           99000.00, '2020-07-22', 'Denver',         0,  8, 26),
( 81, 'Cindy Ellis',       'cindy.ellis@corp.com',       101000.00, '2020-11-04', 'Denver',         0,  8, 26),
( 82, 'Dan Hawkins',       'dan.hawkins@corp.com',        97000.00, '2021-03-17', 'Denver',         0,  8, 26),
(104, 'Zane Morrison',     'zane.morrison@corp.com',      85000.00, '2021-04-18', 'Denver',         0,  8, 25),
-- Data Science (dept 9)
( 83, 'Eve Sanders',       'eve.sanders@corp.com',       115000.00, '2019-07-01', 'San Francisco',  0,  9, 27),
( 84, 'Felix Price',       'felix.price@corp.com',       112000.00, '2020-01-14', 'San Francisco',  0,  9, 27),
( 85, 'Gwen Armstrong',    'gwen.armstrong@corp.com',    118000.00, '2019-10-28', 'San Francisco',  0,  9, 27),
( 86, 'Hugo Owens',        'hugo.owens@corp.com',        113000.00, '2020-04-11', 'San Francisco',  0,  9, 28),
( 87, 'Ivy Spencer',       'ivy.spencer@corp.com',       116000.00, '2020-08-24', 'San Francisco',  0,  9, 28),
( 88, 'John Tucker',       'john.tucker@corp.com',       110000.00, '2021-01-07', 'San Francisco',  0,  9, 28),
( 89, 'Kate Burns',        'kate.burns@corp.com',        119000.00, '2019-05-20', 'San Francisco',  0,  9, 27),
( 90, 'Louis Porter',      'louis.porter@corp.com',      114000.00, '2020-09-15', 'San Francisco',  0,  9, 28),
(103, 'Yolanda Stanley',   'yolanda.stanley@corp.com',   117000.00, '2021-01-05', 'San Francisco',  0,  9, 27),
-- Customer Support (dept 10)
( 91, 'Mary Stone',        'mary.stone@corp.com',         60000.00, '2020-12-01', 'Phoenix',        0, 10, 29),
( 92, 'Neil Hunter',       'neil.hunter@corp.com',        58000.00, '2021-03-15', 'Phoenix',        0, 10, 29),
( 93, 'Olive Gardner',     'olive.gardner@corp.com',      62000.00, '2020-08-28', 'Phoenix',        0, 10, 29),
( 94, 'Pete Warren',       'pete.warren@corp.com',        59000.00, '2021-06-11', 'Phoenix',        0, 10, 30),
( 95, 'Queenie Fisher',    'queenie.fisher@corp.com',     61000.00, '2021-09-24', 'Phoenix',        0, 10, 30),
( 96, 'Randy Cunningham',  'randy.cunningham@corp.com',   57000.00, '2022-01-07', 'Phoenix',        0, 10, 30),
( 97, 'Sandra Marshall',   'sandra.marshall@corp.com',    63000.00, '2020-06-20', 'Phoenix',        0, 10, 29),
( 98, 'Tom Pierce',        'tom.pierce@corp.com',         56000.00, '2021-11-02', 'Phoenix',        0, 10, 30),
(105, 'Ana Walters',       'ana.walters@corp.com',        64000.00, '2021-07-31', 'Phoenix',        0, 10, 29);

-- ═════════════════════════════════════════════════════════════
-- 3. PROJECT  (20 rows)
-- ═════════════════════════════════════════════════════════════
INSERT INTO project (project_id, project_name, start_date, end_date, dept_id) VALUES
( 1, 'Cloud Migration',            '2022-01-01', '2023-06-30',  1),
( 2, 'Employee Portal v2',         '2022-03-01', '2022-12-31',  1),
( 3, 'HR Automation',              '2022-02-01', '2023-01-31',  2),
( 4, 'Talent Analytics',           '2022-06-01', '2023-05-31',  2),
( 5, 'Budget Forecasting Tool',    '2022-01-15', '2022-11-30',  3),
( 6, 'Tax Compliance System',      '2022-04-01', '2023-03-31',  3),
( 7, 'Brand Refresh Campaign',     '2022-03-15', '2022-09-30',  4),
( 8, 'Social Media Analytics',     '2022-07-01', '2023-06-30',  4),
( 9, 'Supply Chain Optimization',  '2022-02-15', '2023-02-28',  5),
(10, 'Vendor Management System',   '2022-05-01', '2023-04-30',  5),
(11, 'CRM Upgrade',                '2022-01-10', '2022-12-15',  6),
(12, 'Sales Forecasting AI',       '2022-08-01', '2023-07-31',  6),
(13, 'Contract Management',        '2022-03-01', '2023-02-28',  7),
(14, 'Compliance Dashboard',       '2022-06-15', '2023-05-15',  7),
(15, 'Product Roadmap Tool',       '2022-02-01', '2022-11-30',  8),
(16, 'Feature Flag System',        '2022-09-01', '2023-08-31',  8),
(17, 'ML Pipeline Platform',       '2022-01-01', '2023-12-31',  9),
(18, 'Customer Churn Model',       '2022-04-01', '2023-03-31',  9),
(19, 'Support Ticket AI',          '2022-03-01', '2022-12-31', 10),
(20, 'Customer Feedback Portal',   '2022-07-01', '2023-06-30', 10);

-- ═════════════════════════════════════════════════════════════
-- 4. EMP_PROJECT  (135 rows, unique emp_id+project_id pairs)
--    Cross-dept assignments intentionally model real-world
--    collaboration (e.g., Data Science supporting Finance).
-- ═════════════════════════════════════════════════════════════
INSERT INTO emp_project (emp_id, project_id, role, allocation) VALUES
-- ── Project 1: Cloud Migration ────────────────────────────
( 1,  1, 'Architect',   30),
(11,  1, 'Lead',        50),
(12,  1, 'Lead',        40),
(31,  1, 'Developer',   80),
(32,  1, 'Developer',   80),
(33,  1, 'Developer',  100),
(83,  1, 'Analyst',     30),
(84,  1, 'Analyst',     40),
-- ── Project 2: Employee Portal v2 ────────────────────────
(12,  2, 'Lead',        60),
(25,  2, 'Consultant',  20),
(34,  2, 'Developer',  100),
(35,  2, 'Developer',  100),
(36,  2, 'Developer',   80),
(37,  2, 'Developer',   60),
(38,  2, 'Tester',     100),
-- ── Project 3: HR Automation ─────────────────────────────
( 2,  3, 'Sponsor',     10),
(11,  3, 'Consultant',  20),
(13,  3, 'Manager',     50),
(14,  3, 'Lead',        60),
(39,  3, 'Analyst',    100),
(40,  3, 'Analyst',     80),
(41,  3, 'Analyst',     60),
-- ── Project 4: Talent Analytics ──────────────────────────
(13,  4, 'Manager',     50),
(27,  4, 'Lead',        40),
(28,  4, 'Developer',   50),
(42,  4, 'Analyst',    100),
(43,  4, 'Analyst',    100),
(44,  4, 'Analyst',     80),
-- ── Project 5: Budget Forecasting Tool ───────────────────
( 3,  5, 'Sponsor',     10),
( 9,  5, 'Consultant',  20),
(15,  5, 'Manager',     60),
(16,  5, 'Lead',        70),
(45,  5, 'Analyst',    100),
(46,  5, 'Analyst',    100),
(47,  5, 'Analyst',     80),
-- ── Project 6: Tax Compliance System ─────────────────────
(15,  6, 'Manager',     40),
(23,  6, 'Consultant',  30),
(24,  6, 'Consultant',  20),
(48,  6, 'Analyst',    100),
(49,  6, 'Analyst',     80),
(50,  6, 'Analyst',     60),
-- ── Project 7: Brand Refresh Campaign ────────────────────
( 4,  7, 'Sponsor',     10),
(17,  7, 'Manager',     60),
(18,  7, 'Lead',        70),
(51,  7, 'Designer',   100),
(52,  7, 'Designer',   100),
(53,  7, 'Designer',    80),
(77,  7, 'Consultant',  20),
-- ── Project 8: Social Media Analytics ────────────────────
(17,  8, 'Manager',     40),
(54,  8, 'Analyst',     80),
(55,  8, 'Analyst',    100),
(56,  8, 'Analyst',     60),
(83,  8, 'Lead',        50),
(103, 8, 'Developer',   70),
-- ── Project 9: Supply Chain Optimization ─────────────────
( 5,  9, 'Sponsor',     10),
(11,  9, 'Consultant',  20),
(19,  9, 'Manager',     50),
(20,  9, 'Lead',        60),
(57,  9, 'Analyst',    100),
(58,  9, 'Analyst',     80),
(59,  9, 'Analyst',     60),
-- ── Project 10: Vendor Management System ─────────────────
(19, 10, 'Manager',     50),
(23, 10, 'Consultant',  20),
(60, 10, 'Analyst',    100),
(61, 10, 'Analyst',     80),
(62, 10, 'Analyst',    100),
-- ── Project 11: CRM Upgrade ───────────────────────────────
( 6, 11, 'Sponsor',     10),
(11, 11, 'Consultant',  20),
(21, 11, 'Manager',     50),
(22, 11, 'Lead',        60),
(63, 11, 'Developer',   80),
(64, 11, 'Developer',  100),
(65, 11, 'Tester',      60),
-- ── Project 12: Sales Forecasting AI ─────────────────────
(21, 12, 'Manager',     50),
(27, 12, 'Lead',        50),
(66, 12, 'Analyst',    100),
(67, 12, 'Analyst',     80),
(68, 12, 'Developer',   60),
(87, 12, 'Developer',   60),
-- ── Project 13: Contract Management ──────────────────────
( 7, 13, 'Sponsor',     10),
(23, 13, 'Manager',     50),
(24, 13, 'Lead',        60),
(45, 13, 'Consultant',  20),
(71, 13, 'Consultant', 100),
(72, 13, 'Analyst',     80),
(73, 13, 'Analyst',     60),
-- ── Project 14: Compliance Dashboard ─────────────────────
( 3, 14, 'Consultant',  20),
(15, 14, 'Consultant',  30),
(23, 14, 'Manager',     50),
(74, 14, 'Analyst',     80),
(75, 14, 'Analyst',    100),
(76, 14, 'Developer',   60),
-- ── Project 15: Product Roadmap Tool ─────────────────────
( 4, 15, 'Consultant',  20),
( 8, 15, 'Sponsor',     10),
(25, 15, 'Manager',     50),
(26, 15, 'Lead',        60),
(77, 15, 'Analyst',     80),
(78, 15, 'Analyst',    100),
(79, 15, 'Designer',    80),
-- ── Project 16: Feature Flag System ──────────────────────
(11, 16, 'Consultant',  20),
(25, 16, 'Manager',     40),
(31, 16, 'Developer',   50),
(80, 16, 'Developer',  100),
(81, 16, 'Developer',   80),
(82, 16, 'Developer',  100),
-- ── Project 17: ML Pipeline Platform ─────────────────────
( 9, 17, 'Sponsor',     10),
(27, 17, 'Manager',     40),
(28, 17, 'Lead',        60),
(31, 17, 'Consultant',  20),
(83, 17, 'Developer',   70),
(84, 17, 'Developer',   80),
(85, 17, 'Developer',  100),
(86, 17, 'Analyst',     80),
-- ── Project 18: Customer Churn Model ─────────────────────
(18, 18, 'Consultant',  20),
(27, 18, 'Lead',        60),
(87, 18, 'Developer',   80),
(88, 18, 'Developer',  100),
(89, 18, 'Analyst',     80),
(90, 18, 'Analyst',     60),
(103,18, 'Developer',   50),
-- ── Project 19: Support Ticket AI ────────────────────────
(10, 19, 'Sponsor',     10),
(27, 19, 'Consultant',  30),
(29, 19, 'Manager',     50),
(30, 19, 'Lead',        60),
(91, 19, 'Analyst',     80),
(92, 19, 'Developer',  100),
(93, 19, 'Tester',      60),
-- ── Project 20: Customer Feedback Portal ─────────────────
( 4, 20, 'Consultant',  10),
(29, 20, 'Manager',     50),
(94, 20, 'Analyst',     80),
(95, 20, 'Analyst',    100),
(96, 20, 'Developer',   60),
(97, 20, 'Tester',      80),
(98, 20, 'Developer',  100),
(105,20, 'Analyst',     60);

-- ═════════════════════════════════════════════════════════════
-- 5. SALARY_HISTORY  (110 rows)
--    emp 1–30  → 2 records each (60 rows): shows a pay raise
--    emp 31–80 → 1 record each  (50 rows): joined since 2019+
-- ═════════════════════════════════════════════════════════════
INSERT INTO salary_history (sal_id, salary, from_date, to_date, emp_id) VALUES
-- Directors: 2 records each
(  1, 120000.00, '2015-03-01', '2019-06-30',  1),
(  2, 150000.00, '2019-07-01',          NULL,  1),
(  3, 115000.00, '2014-07-15', '2018-12-31',  2),
(  4, 145000.00, '2019-01-01',          NULL,  2),
(  5, 118000.00, '2013-11-20', '2018-10-31',  3),
(  6, 148000.00, '2018-11-01',          NULL,  3),
(  7, 112000.00, '2016-01-10', '2020-06-30',  4),
(  8, 142000.00, '2020-07-01',          NULL,  4),
(  9, 110000.00, '2015-06-25', '2019-12-31',  5),
( 10, 140000.00, '2020-01-01',          NULL,  5),
( 11, 120000.00, '2012-09-05', '2018-08-31',  6),
( 12, 155000.00, '2018-09-01',          NULL,  6),
( 13, 110000.00, '2017-04-18', '2021-03-31',  7),
( 14, 138000.00, '2021-04-01',          NULL,  7),
( 15, 113000.00, '2016-08-30', '2020-12-31',  8),
( 16, 143000.00, '2021-01-01',          NULL,  8),
( 17, 125000.00, '2014-02-14', '2019-01-31',  9),
( 18, 160000.00, '2019-02-01',          NULL,  9),
( 19, 105000.00, '2018-05-22', '2022-04-30', 10),
( 20, 130000.00, '2022-05-01',          NULL, 10),
-- Senior Managers: 2 records each
( 21,  88000.00, '2017-03-14', '2020-12-31', 11),
( 22, 110000.00, '2021-01-01',          NULL, 11),
( 23,  85000.00, '2018-07-20', '2021-06-30', 12),
( 24, 108000.00, '2021-07-01',          NULL, 12),
( 25,  82000.00, '2019-01-08', '2022-01-31', 13),
( 26, 105000.00, '2022-02-01',          NULL, 13),
( 27,  84000.00, '2018-11-12', '2021-10-31', 14),
( 28, 107000.00, '2021-11-01',          NULL, 14),
( 29,  90000.00, '2017-06-30', '2021-05-31', 15),
( 30, 112000.00, '2021-06-01',          NULL, 15),
( 31,  86000.00, '2018-02-14', '2021-01-31', 16),
( 32, 109000.00, '2021-02-01',          NULL, 16),
( 33,  83000.00, '2019-03-25', '2022-03-31', 17),
( 34, 106000.00, '2022-04-01',          NULL, 17),
( 35,  82000.00, '2020-01-05', '2022-12-31', 18),
( 36, 104000.00, '2023-01-01',          NULL, 18),
( 37,  88000.00, '2017-09-18', '2020-08-31', 19),
( 38, 111000.00, '2020-09-01',          NULL, 19),
( 39,  80000.00, '2019-07-22', '2022-06-30', 20),
( 40, 103000.00, '2022-07-01',          NULL, 20),
( 41,  90000.00, '2016-12-10', '2020-11-30', 21),
( 42, 115000.00, '2020-12-01',          NULL, 21),
( 43,  89000.00, '2017-05-16', '2021-04-30', 22),
( 44, 113000.00, '2021-05-01',          NULL, 22),
( 45,  85000.00, '2018-04-02', '2021-03-31', 23),
( 46, 108000.00, '2021-04-01',          NULL, 23),
( 47,  83000.00, '2019-09-11', '2022-08-31', 24),
( 48, 106000.00, '2022-09-01',          NULL, 24),
( 49,  88000.00, '2017-11-28', '2020-10-31', 25),
( 50, 112000.00, '2020-11-01',          NULL, 25),
( 51,  87000.00, '2018-06-14', '2021-05-31', 26),
( 52, 110000.00, '2021-06-01',          NULL, 26),
( 53,  95000.00, '2016-08-07', '2020-07-31', 27),
( 54, 120000.00, '2020-08-01',          NULL, 27),
( 55,  93000.00, '2017-10-19', '2021-09-30', 28),
( 56, 118000.00, '2021-10-01',          NULL, 28),
( 57,  78000.00, '2019-04-30', '2022-04-30', 29),
( 58, 100000.00, '2022-05-01',          NULL, 29),
( 59,  76000.00, '2020-02-17', '2023-01-31', 30),
( 60,  98000.00, '2023-02-01',          NULL, 30),
-- Individual Contributors: 1 record each
( 61,  85000.00, '2020-06-01', NULL,  31),
( 62,  82000.00, '2021-01-15', NULL,  32),
( 63,  88000.00, '2020-09-20', NULL,  33),
( 64,  84000.00, '2021-03-10', NULL,  34),
( 65,  86000.00, '2021-07-05', NULL,  35),
( 66,  81000.00, '2022-01-20', NULL,  36),
( 67,  87000.00, '2020-11-11', NULL,  37),
( 68,  83000.00, '2021-05-28', NULL,  38),
( 69,  72000.00, '2020-08-03', NULL,  39),
( 70,  70000.00, '2021-02-22', NULL,  40),
( 71,  74000.00, '2020-10-14', NULL,  41),
( 72,  71000.00, '2021-06-07', NULL,  42),
( 73,  73000.00, '2021-09-18', NULL,  43),
( 74,  69000.00, '2022-03-01', NULL,  44),
( 75,  90000.00, '2019-12-01', NULL,  45),
( 76,  88000.00, '2020-04-15', NULL,  46),
( 77,  92000.00, '2019-08-20', NULL,  47),
( 78,  89000.00, '2020-07-10', NULL,  48),
( 79,  91000.00, '2021-01-25', NULL,  49),
( 80,  87000.00, '2021-05-12', NULL,  50),
( 81,  78000.00, '2020-09-08', NULL,  51),
( 82,  76000.00, '2021-02-14', NULL,  52),
( 83,  80000.00, '2020-11-30', NULL,  53),
( 84,  77000.00, '2021-04-19', NULL,  54),
( 85,  79000.00, '2021-08-05', NULL,  55),
( 86,  75000.00, '2022-01-10', NULL,  56),
( 87,  68000.00, '2020-07-14', NULL,  57),
( 88,  66000.00, '2021-01-08', NULL,  58),
( 89,  70000.00, '2020-10-22', NULL,  59),
( 90,  67000.00, '2021-03-16', NULL,  60),
( 91,  69000.00, '2021-07-29', NULL,  61),
( 92,  65000.00, '2022-02-04', NULL,  62),
( 93,  95000.00, '2019-11-11', NULL,  63),
( 94,  93000.00, '2020-03-25', NULL,  64),
( 95,  97000.00, '2019-07-08', NULL,  65),
( 96,  94000.00, '2020-06-17', NULL,  66),
( 97,  96000.00, '2020-09-30', NULL,  67),
( 98,  92000.00, '2021-01-14', NULL,  68),
( 99,  98000.00, '2019-05-20', NULL,  69),
(100,  90000.00, '2020-08-12', NULL,  70),
(101,  95000.00, '2019-10-05', NULL,  71),
(102,  93000.00, '2020-02-18', NULL,  72),
(103,  97000.00, '2019-06-11', NULL,  73),
(104,  94000.00, '2020-05-24', NULL,  74),
(105,  96000.00, '2020-09-08', NULL,  75),
(106,  92000.00, '2021-01-21', NULL,  76),
(107, 100000.00, '2019-08-14', NULL,  77),
(108,  98000.00, '2020-01-27', NULL,  78),
(109, 102000.00, '2019-04-09', NULL,  79),
(110,  99000.00, '2020-07-22', NULL,  80);

-- ═════════════════════════════════════════════════════════════
-- 6. AUDIT_LOG  (110 rows)
--    id is IDENTITY — the DB assigns it automatically.
--    Covers a mix of CREATE / UPDATE / DELETE across all entities.
-- ═════════════════════════════════════════════════════════════
INSERT INTO audit_log (`action`, entity_name, entity_id, details, `timestamp`) VALUES
-- ── Employee CREATES (onboarding) ────────────────────────────
('CREATE', 'Employee',    1,  'Onboarded Alice Johnson as VP Engineering',                    '2015-03-01 09:00:00'),
('CREATE', 'Employee',    2,  'Onboarded Bob Martinez as VP Human Resources',                 '2014-07-15 09:00:00'),
('CREATE', 'Employee',    3,  'Onboarded Carol White as VP Finance',                          '2013-11-20 09:00:00'),
('CREATE', 'Employee',    4,  'Onboarded David Kim as VP Marketing',                          '2016-01-10 09:00:00'),
('CREATE', 'Employee',    5,  'Onboarded Emma Davis as VP Operations',                        '2015-06-25 09:00:00'),
('CREATE', 'Employee',    6,  'Onboarded Frank Wilson as VP Sales',                           '2012-09-05 09:00:00'),
('CREATE', 'Employee',    7,  'Onboarded Grace Lee as VP Legal',                              '2017-04-18 09:00:00'),
('CREATE', 'Employee',    8,  'Onboarded Henry Brown as VP Product Management',               '2016-08-30 09:00:00'),
('CREATE', 'Employee',    9,  'Onboarded Iris Chen as VP Data Science',                       '2014-02-14 09:00:00'),
('CREATE', 'Employee',   10,  'Onboarded Jack Taylor as VP Customer Support',                 '2018-05-22 09:00:00'),
('CREATE', 'Employee',   11,  'Onboarded Karen Anderson as Engineering Manager',              '2017-03-14 09:00:00'),
('CREATE', 'Employee',   12,  'Onboarded Leo Thompson as Engineering Manager',                '2018-07-20 09:00:00'),
('CREATE', 'Employee',   13,  'Onboarded Mia Roberts as HR Manager',                         '2019-01-08 09:00:00'),
('CREATE', 'Employee',   14,  'Onboarded Noah Harris as HR Manager',                         '2018-11-12 09:00:00'),
('CREATE', 'Employee',   15,  'Onboarded Olivia Scott as Finance Manager',                   '2017-06-30 09:00:00'),
('CREATE', 'Employee',   21,  'Onboarded Umar Hall as Sales Manager',                        '2016-12-10 09:00:00'),
('CREATE', 'Employee',   27,  'Onboarded Aaron Carter as Data Science Manager',              '2016-08-07 09:00:00'),
('CREATE', 'Employee',   31,  'Onboarded Ethan Parker as Software Engineer',                 '2020-06-01 09:00:00'),
('CREATE', 'Employee',   39,  'Onboarded Mike Bailey as HR Specialist',                      '2020-08-03 09:00:00'),
('CREATE', 'Employee',   45,  'Onboarded Steve Howard as Financial Analyst',                 '2019-12-01 09:00:00'),
('CREATE', 'Employee',   63,  'Onboarded Kim Powell as Account Executive',                   '2019-11-11 09:00:00'),
('CREATE', 'Employee',   71,  'Onboarded Sean Bryant as Corporate Counsel',                  '2019-10-05 09:00:00'),
('CREATE', 'Employee',   83,  'Onboarded Eve Sanders as Data Scientist',                     '2019-07-01 09:00:00'),
('CREATE', 'Employee',   91,  'Onboarded Mary Stone as Support Specialist',                  '2020-12-01 09:00:00'),
('CREATE', 'Employee',   99,  'Onboarded Uma Dawson as Senior Engineer',                     '2021-02-14 09:00:00'),
('CREATE', 'Employee',  100,  'Onboarded Vince Bradley as Senior Engineer',                  '2020-05-27 09:00:00'),
('CREATE', 'Employee',  101,  'Onboarded Wanda Griffith as Marketing Analyst',               '2021-08-09 09:00:00'),
('CREATE', 'Employee',  102,  'Onboarded Xander Fletcher as Senior AE',                     '2020-11-22 09:00:00'),
('CREATE', 'Employee',  103,  'Onboarded Yolanda Stanley as ML Engineer',                   '2021-01-05 09:00:00'),
('CREATE', 'Employee',  104,  'Onboarded Zane Morrison as Product Analyst',                 '2021-04-18 09:00:00'),
('CREATE', 'Employee',  105,  'Onboarded Ana Walters as Support Specialist',                '2021-07-31 09:00:00'),
-- ── Department CREATES ────────────────────────────────────────
('CREATE', 'Department',  1,  'Engineering department created with budget 5000000',           '2010-01-01 08:00:00'),
('CREATE', 'Department',  2,  'Human Resources department created with budget 1500000',       '2010-01-01 08:05:00'),
('CREATE', 'Department',  3,  'Finance department created with budget 2000000',               '2010-01-01 08:10:00'),
('CREATE', 'Department',  4,  'Marketing department created with budget 1800000',             '2010-01-01 08:15:00'),
('CREATE', 'Department',  5,  'Operations department created with budget 2500000',            '2010-01-01 08:20:00'),
('CREATE', 'Department',  6,  'Sales department created with budget 3000000',                 '2010-01-01 08:25:00'),
('CREATE', 'Department',  7,  'Legal department created with budget 1200000',                 '2012-03-15 08:00:00'),
('CREATE', 'Department',  8,  'Product Management department created with budget 2200000',    '2015-06-01 08:00:00'),
('CREATE', 'Department',  9,  'Data Science department created with budget 3500000',          '2018-01-10 08:00:00'),
('CREATE', 'Department', 10,  'Customer Support department created with budget 1000000',      '2019-04-01 08:00:00'),
-- ── Project CREATES ───────────────────────────────────────────
('CREATE', 'Project',     1,  'Cloud Migration project initiated, lead: Karen Anderson',      '2022-01-01 10:00:00'),
('CREATE', 'Project',     2,  'Employee Portal v2 project started, lead: Leo Thompson',       '2022-03-01 10:00:00'),
('CREATE', 'Project',     3,  'HR Automation project started, lead: Noah Harris',             '2022-02-01 10:00:00'),
('CREATE', 'Project',     4,  'Talent Analytics project started, lead: Aaron Carter',         '2022-06-01 10:00:00'),
('CREATE', 'Project',     5,  'Budget Forecasting Tool project started, lead: Peter Evans',   '2022-01-15 10:00:00'),
('CREATE', 'Project',     6,  'Tax Compliance System project started',                        '2022-04-01 10:00:00'),
('CREATE', 'Project',     7,  'Brand Refresh Campaign initiated, lead: Rachel Green',         '2022-03-15 10:00:00'),
('CREATE', 'Project',     8,  'Social Media Analytics project started',                       '2022-07-01 10:00:00'),
('CREATE', 'Project',     9,  'Supply Chain Optimization project initiated',                  '2022-02-15 10:00:00'),
('CREATE', 'Project',    10,  'Vendor Management System project started',                     '2022-05-01 10:00:00'),
('CREATE', 'Project',    17,  'ML Pipeline Platform: flagship Data Science project',          '2022-01-01 10:00:00'),
('CREATE', 'Project',    18,  'Customer Churn Model project started',                         '2022-04-01 10:00:00'),
('CREATE', 'Project',    19,  'Support Ticket AI project started',                            '2022-03-01 10:00:00'),
('CREATE', 'Project',    20,  'Customer Feedback Portal project started',                     '2022-07-01 10:00:00'),
-- ── Employee UPDATES (salary changes) ────────────────────────
('UPDATE', 'Employee',    1,  'Salary updated from 120000 to 150000 — annual review',         '2019-07-01 11:00:00'),
('UPDATE', 'Employee',    2,  'Salary updated from 115000 to 145000 — annual review',         '2019-01-01 11:00:00'),
('UPDATE', 'Employee',    3,  'Salary updated from 118000 to 148000 — annual review',         '2018-11-01 11:00:00'),
('UPDATE', 'Employee',    4,  'Salary updated from 112000 to 142000 — promotion',             '2020-07-01 11:00:00'),
('UPDATE', 'Employee',    6,  'Salary updated from 120000 to 155000 — exceptional performance','2018-09-01 11:00:00'),
('UPDATE', 'Employee',    9,  'Salary updated from 125000 to 160000 — retention bonus',       '2019-02-01 11:00:00'),
('UPDATE', 'Employee',   11,  'Salary updated from 88000 to 110000 — promotion to Sr Manager','2021-01-01 11:00:00'),
('UPDATE', 'Employee',   15,  'Salary updated from 90000 to 112000 — annual review',          '2021-06-01 11:00:00'),
('UPDATE', 'Employee',   21,  'Salary updated from 90000 to 115000 — promotion',              '2020-12-01 11:00:00'),
('UPDATE', 'Employee',   27,  'Salary updated from 95000 to 120000 — annual review',          '2020-08-01 11:00:00'),
-- ── Employee UPDATES (department transfer) ────────────────────
('UPDATE', 'Employee',   55,  'Transferred from dept 5 (Operations) to dept 4 (Marketing)',   '2021-08-05 14:00:00'),
('UPDATE', 'Employee',   68,  'Location updated from Austin to Seattle — relocation',          '2021-06-01 09:30:00'),
('UPDATE', 'Employee',   76,  'Location updated from New York to Boston',                      '2021-05-15 09:00:00'),
('UPDATE', 'Employee',   84,  'Role updated to Senior Data Scientist',                         '2022-01-14 10:00:00'),
('UPDATE', 'Employee',   89,  'Promoted to Lead Data Scientist',                               '2022-05-20 10:00:00'),
('UPDATE', 'Employee',   97,  'Updated email address — company domain change',                 '2022-03-01 08:00:00'),
-- ── Project UPDATES ───────────────────────────────────────────
('UPDATE', 'Project',     1,  'Cloud Migration end date extended to 2023-06-30',              '2022-10-15 10:00:00'),
('UPDATE', 'Project',     5,  'Budget Forecasting Tool scope reduced, end date brought forward','2022-09-01 10:00:00'),
('UPDATE', 'Project',     7,  'Brand Refresh Campaign: additional designer allocated',         '2022-06-01 14:00:00'),
('UPDATE', 'Project',    11,  'CRM Upgrade: integration scope extended by 2 months',          '2022-07-20 10:00:00'),
('UPDATE', 'Project',    13,  'Contract Management: legal review phase added',                 '2022-11-15 10:00:00'),
('UPDATE', 'Project',    17,  'ML Pipeline Platform: GPU cluster capacity doubled',            '2022-09-10 10:00:00'),
-- ── Department UPDATES (budget revisions) ────────────────────
('UPDATE', 'Department',  1,  'Engineering budget increased to 5500000 — headcount growth',   '2022-04-01 08:00:00'),
('UPDATE', 'Department',  9,  'Data Science budget increased to 4000000 — AI initiative',     '2022-01-15 08:00:00'),
('UPDATE', 'Department',  6,  'Sales budget revised down to 2800000 — cost optimisation',     '2022-08-01 08:00:00'),
('UPDATE', 'Department',  3,  'Finance budget increased to 2200000 — compliance investment',  '2022-03-10 08:00:00'),
-- ── EmployeeProject UPDATES ───────────────────────────────────
('UPDATE', 'EmployeeProject', 31, 'Ethan Parker allocation on Cloud Migration raised to 100', '2022-06-01 09:00:00'),
('UPDATE', 'EmployeeProject', 83, 'Eve Sanders role on Cloud Migration changed to Tech Lead', '2022-05-01 09:00:00'),
('UPDATE', 'EmployeeProject', 27, 'Aaron Carter allocation on Talent Analytics increased',    '2022-09-01 09:00:00'),
('UPDATE', 'EmployeeProject', 17, 'Quinn Foster allocation on Brand Refresh increased to 80', '2022-07-01 09:00:00'),
('UPDATE', 'EmployeeProject', 85, 'Gwen Armstrong promoted to Lead on ML Pipeline',          '2022-10-01 09:00:00'),
-- ── Employee DELETES (offboarding) ───────────────────────────
('DELETE', 'Employee',  110,  'Offboarded employee 110 — contract ended',                     '2023-02-28 17:00:00'),
('DELETE', 'Employee',  111,  'Offboarded employee 111 — voluntary resignation',              '2023-03-15 17:00:00'),
('DELETE', 'Employee',  112,  'Offboarded employee 112 — position eliminated',                '2023-04-01 17:00:00'),
-- ── Project DELETES (cancelled projects) ─────────────────────
('DELETE', 'Project',    21,  'Project cancelled — funding withdrawn before kickoff',         '2022-11-30 16:00:00'),
('DELETE', 'Project',    22,  'Project cancelled — merged into ML Pipeline Platform (id=17)', '2022-12-15 16:00:00'),
-- ── SalaryHistory CREATES ─────────────────────────────────────
('CREATE', 'SalaryHistory',  1,  'Salary history record created for emp 1 on join',          '2015-03-01 09:05:00'),
('CREATE', 'SalaryHistory',  2,  'Salary history record updated for emp 1 after raise',      '2019-07-01 11:05:00'),
('CREATE', 'SalaryHistory',  3,  'Salary history record created for emp 2 on join',          '2014-07-15 09:05:00'),
('CREATE', 'SalaryHistory',  4,  'Salary history record updated for emp 2 after raise',      '2019-01-01 11:05:00'),
('CREATE', 'SalaryHistory', 11,  'Salary history created for emp 6 (Frank Wilson) on join',  '2012-09-05 09:05:00'),
('CREATE', 'SalaryHistory', 12,  'Salary history updated for emp 6 after promotion',         '2018-09-01 11:05:00'),
('CREATE', 'SalaryHistory', 17,  'Salary history created for emp 9 (Iris Chen) on join',     '2014-02-14 09:05:00'),
('CREATE', 'SalaryHistory', 18,  'Salary history updated for emp 9 after retention raise',   '2019-02-01 11:05:00'),
('CREATE', 'SalaryHistory', 61,  'Salary history record created for emp 31 on join',         '2020-06-01 09:05:00'),
('CREATE', 'SalaryHistory', 93,  'Salary history record created for emp 63 on join',         '2019-11-11 09:05:00'),
('CREATE', 'SalaryHistory', 95,  'Salary history record created for emp 65 on join',         '2019-07-08 09:05:00'),
('CREATE', 'SalaryHistory', 99,  'Salary history record created for emp 69 on join',         '2019-05-20 09:05:00'),
-- ── Security / system audit entries ──────────────────────────
('UPDATE', 'Employee',   32,  'Password reset requested by admin',                            '2022-05-10 08:30:00'),
('UPDATE', 'Employee',   47,  'MFA enrollment completed',                                     '2022-06-15 09:00:00'),
('UPDATE', 'Employee',   58,  'Account unlocked after failed login attempts',                 '2022-07-20 08:45:00'),
('UPDATE', 'Employee',   73,  'Role permission elevated to view financial reports',           '2022-08-01 10:00:00'),
('UPDATE', 'Employee',   88,  'API access token rotated for ML system integration',           '2022-09-05 11:00:00'),
('UPDATE', 'Employee',   92,  'Support tier access upgraded to Level 2',                     '2022-10-12 09:00:00'),
-- ── Year-end batch audit entries ──────────────────────────────
('UPDATE', 'Employee',   33,  'Year-end performance review completed — rating: Exceeds',      '2022-12-15 16:00:00'),
('UPDATE', 'Employee',   46,  'Year-end performance review completed — rating: Meets',        '2022-12-15 16:05:00'),
('UPDATE', 'Employee',   57,  'Year-end performance review completed — rating: Exceeds',      '2022-12-15 16:10:00'),
('UPDATE', 'Employee',   64,  'Year-end performance review completed — rating: Below',        '2022-12-15 16:15:00'),
('UPDATE', 'Employee',   72,  'Year-end performance review completed — rating: Meets',        '2022-12-15 16:20:00'),
('UPDATE', 'Employee',   80,  'Year-end performance review completed — rating: Exceeds',      '2022-12-15 16:25:00'),
('UPDATE', 'Employee',   86,  'Year-end performance review completed — rating: Meets',        '2022-12-15 16:30:00'),
('UPDATE', 'Employee',   93,  'Year-end performance review completed — rating: Exceeds',      '2022-12-15 16:35:00'),
('UPDATE', 'Employee',   98,  'Year-end performance review completed — rating: Below',        '2022-12-15 16:40:00'),
('UPDATE', 'Employee',  104,  'Year-end performance review completed — rating: Meets',        '2022-12-15 16:45:00'),
-- ── Miscellaneous operational audits ─────────────────────────
('CREATE', 'EmployeeProject', 102, 'Xander Fletcher assigned to CRM Upgrade as Developer',   '2022-02-01 10:00:00'),
('CREATE', 'EmployeeProject', 103, 'Yolanda Stanley assigned to Customer Churn Model',       '2022-04-15 10:00:00'),
('CREATE', 'EmployeeProject', 104, 'Zane Morrison assigned to Product Roadmap Tool',         '2022-04-20 10:00:00'),
('CREATE', 'EmployeeProject', 101, 'Wanda Griffith assigned to Brand Refresh Campaign',      '2022-04-01 10:00:00'),
('DELETE', 'EmployeeProject', 37,  'Kevin Murphy removed from Cloud Migration — reassigned', '2022-08-31 17:00:00'),
('UPDATE', 'Department',  10,  'Customer Support budget raised to 1100000 — ticket surge',   '2022-11-01 08:00:00'),
('UPDATE', 'Department',   4,  'Marketing budget increased to 2000000 — product launch',     '2022-10-01 08:00:00'),
('UPDATE', 'Department',   7,  'Legal budget increased to 1400000 — M&A activity',           '2022-09-01 08:00:00'),
('CREATE', 'Project',    11,  'CRM Upgrade kickoff meeting held, all stakeholders confirmed', '2022-01-10 14:00:00'),
('UPDATE', 'Project',     9,  'Supply Chain Optimization: vendor integration phase complete', '2022-11-01 10:00:00'),
('UPDATE', 'Project',    18,  'Customer Churn Model: v1 deployed to production',             '2022-12-01 10:00:00'),
('UPDATE', 'Project',    19,  'Support Ticket AI: beta testing phase started',               '2022-09-01 10:00:00'),
('UPDATE', 'Project',    20,  'Customer Feedback Portal: UAT sign-off received',             '2022-11-15 10:00:00'),
('UPDATE', 'Employee',   29,  'Chris Nelson promoted to Head of Support Operations',         '2022-05-01 09:00:00'),
('UPDATE', 'Employee',   48,  'Victor Jenkins completed CPA certification',                  '2022-07-15 09:00:00'),
('CREATE', 'SalaryHistory', 57, 'New salary history created for Chris Nelson post-promotion','2022-05-01 11:00:00'),
('CREATE', 'SalaryHistory', 59, 'New salary history created for Diana Mitchell',             '2020-02-17 09:05:00');

-- ═════════════════════════════════════════════════════════════
-- 7. RESET AUTO_INCREMENT
--    Set AUTO_INCREMENT well above the highest manually-inserted ID
--    to prevent PK clashes when the application creates new rows.
-- ═════════════════════════════════════════════════════════════
ALTER TABLE department     AUTO_INCREMENT = 1000;
ALTER TABLE employee       AUTO_INCREMENT = 1000;
ALTER TABLE project        AUTO_INCREMENT = 1000;
ALTER TABLE emp_project    AUTO_INCREMENT = 1000;
ALTER TABLE salary_history AUTO_INCREMENT = 1000;
ALTER TABLE audit_log      AUTO_INCREMENT = 1000;

COMMIT;

-- ─────────────────────────────────────────────────────────────
-- Quick sanity-check queries (run manually after seeding)
-- ─────────────────────────────────────────────────────────────
-- SELECT COUNT(*) FROM department;          -- expect 10
-- SELECT COUNT(*) FROM employee;            -- expect 105
-- SELECT COUNT(*) FROM project;             -- expect 20
-- SELECT COUNT(*) FROM emp_project;         -- expect 135
-- SELECT COUNT(*) FROM salary_history;      -- expect 110
-- SELECT COUNT(*) FROM audit_log;           -- expect 110
