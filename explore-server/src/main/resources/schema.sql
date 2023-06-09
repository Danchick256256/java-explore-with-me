CREATE TABLE IF NOT EXISTS users (
  user_id BIGINT GENERATED ALWAYS AS IDENTITY CONSTRAINT u_pk PRIMARY KEY,
  user_email VARCHAR NOT NULL CONSTRAINT u_email_pk UNIQUE,
  user_name VARCHAR NOT NULL CONSTRAINT u_name_pk UNIQUE
);
CREATE TABLE IF NOT EXISTS categories (
  category_id BIGINT GENERATED ALWAYS AS IDENTITY CONSTRAINT ct_pk PRIMARY KEY,
  category_name VARCHAR NOT NULL CONSTRAINT ct_name_pk UNIQUE
);
CREATE TABLE IF NOT EXISTS locations (
  location_id BIGINT GENERATED ALWAYS AS IDENTITY CONSTRAINT l_pk PRIMARY KEY,
  latitude DOUBLE PRECISION NOT NULL,
  longitude DOUBLE PRECISION NOT NULL
);
CREATE TABLE IF NOT EXISTS events (
  event_id BIGINT GENERATED ALWAYS AS IDENTITY CONSTRAINT e_pk PRIMARY KEY,
  event_annotation VARCHAR NOT NULL,
  event_category_id BIGINT NOT NULL CONSTRAINT e_cat_fk REFERENCES categories ON UPDATE CASCADE ON DELETE CASCADE,
  event_confirmed_request INTEGER,
  event_created_on TIMESTAMP, event_description VARCHAR,
  event_date TIMESTAMP NOT NULL, event_initiator BIGINT NOT NULL CONSTRAINT e_owner_fk REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE,
  event_location BIGINT NOT NULL CONSTRAINT e_locations_fk REFERENCES locations ON UPDATE CASCADE ON DELETE CASCADE,
  event_paid BOOLEAN NOT NULL, event_participant_limit INTEGER DEFAULT 0,
  event_published_on TIMESTAMP WITHOUT TIME ZONE,
  event_request_moderation BOOLEAN DEFAULT TRUE,
  event_state VARCHAR, event_title VARCHAR NOT NULL,
  event_views BIGINT
);
CREATE TABLE IF NOT EXISTS compilations (
  compilation_id BIGINT GENERATED ALWAYS AS IDENTITY CONSTRAINT cp_pk PRIMARY KEY,
  compilation_pinned BOOLEAN NOT NULL,
  compilation_title VARCHAR
);

CREATE TABLE IF NOT EXISTS compilation_events (
  compilation_events_id BIGINT GENERATED ALWAYS AS IDENTITY CONSTRAINT ce_pk PRIMARY KEY,
  compilation_events_compilation_id BIGINT NOT NULL CONSTRAINT ce_comp_fk REFERENCES compilations ON UPDATE CASCADE ON DELETE CASCADE,
  compilation_events_event_id BIGINT NOT NULL CONSTRAINT ce_event_fk REFERENCES events ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS requests (
  requests_id BIGINT GENERATED ALWAYS AS IDENTITY CONSTRAINT rq_pk PRIMARY KEY,
  requests_requester BIGINT NOT NULL CONSTRAINT rq_user_fk REFERENCES users ON UPDATE CASCADE ON DELETE CASCADE,
  requests_event BIGINT NOT NULL CONSTRAINT rq_event_fk REFERENCES events ON UPDATE CASCADE ON DELETE CASCADE,
  requests_created TIMESTAMP NOT NULL,
  requests_status VARCHAR NOT NULL
);

