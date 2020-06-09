use db_itmo

db.dropDatabase();
db.createCollection("persons", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["last_name", "first_name", "is_in_dormitory", "warning_count", "person_number"],
            properties: {
                last_name: {
                    bsonType: "string",
                    description: "must be a string and is required"
                },
                first_name: {
                    bsonType: "string",
                    description: "must be a string and is required"
                },
                patronymic_name: {
                    bsonType: "string",
                    description: "must be a string"
                },
                is_in_dormitory: {
                    bsonType: "bool",
                    description: "must be boolean"
                },
                warning_count: {
                    bsonType: "int",
                    description: "must be whole int"
                },
                person_number: {
                    bsonType: "string"
                }
            }
        }
    }
});

db.createCollection("dormitories", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["address", "room_count"],
            properties: {
                address: {
                    bsonType: "string",
                    description: "must be a string and is required"
                },
                room_count: {
                    bsonType: "int",
                    description: "must be a whole int"
                }
            }
        }
    }
});

db.createCollection("rooms", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["room_number", "capacity", "dormitory_id"],
            properties: {
                room_number: {
                    bsonType: "int",
                    description: "must be a whole int"
                },
                capacity: {
                    bsonType: "int"
                },
                engaged: {
                    bsonType: "int"
                },
                bugs: {
                    bsonType: "bool"
                },
                last_cleaning_date: {
                    bsonType: "date"
                },
                dormitory_id: {
                    bsonType: "string",
                    description: "is required and should reference dormitories"
                }
            }
        }
    }
});

db.createCollection("room_records", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["facilities", "budget", "payment", "living_start_date",
                "living_end_date", "course", "person_id", "room_id"],
            properties: {
                facilities: {
                    bsonType: "bool"
                },
                budget: {
                    bsonType: "bool"
                },
                payment: {
                    bsonType: "double"
                },
                living_start_date: {
                    bsonType: "date"
                },
                living_end_date: {
                    bsonType: "date"
                },
                course: {
                    bsonType: "string"
                },
                person_id: {
                    bsonType: "string",
                },
                room_id: {
                    bsonType: "string"
                }
            }
        }
    }
});
