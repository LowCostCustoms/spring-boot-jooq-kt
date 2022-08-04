insert into tags(id, name)
values
    ('7edcce8c-1cb1-4526-a618-8a4c184a381d', 'cat'),
    ('f93773e5-6864-4f11-8171-54035f19c066', 'dog'),
    ('ed494268-0434-44c4-a1b9-faec557df770', 'brown'),
    ('731f6863-a2af-4746-a066-32bdbc5d4dec', 'white');

insert into pets(id, name, status)
values
    ('84e54311-6200-4101-8946-bbd1895d4278', 'Puss in boots', 'Available'),
    ('42e4b462-8ff2-43a5-a710-80876838592c', 'Laika', 'Sold');

insert into pet_tags(pet_id, tag_id)
values
    ('84e54311-6200-4101-8946-bbd1895d4278', '7edcce8c-1cb1-4526-a618-8a4c184a381d'),
    ('84e54311-6200-4101-8946-bbd1895d4278', 'ed494268-0434-44c4-a1b9-faec557df770'),
    ('42e4b462-8ff2-43a5-a710-80876838592c', '731f6863-a2af-4746-a066-32bdbc5d4dec'),
    ('42e4b462-8ff2-43a5-a710-80876838592c', 'f93773e5-6864-4f11-8171-54035f19c066');
