SUBDIR = api

all:
	$(MAKE) -C $(SUBDIR)
	
	docker compose up --build -d

.PHONY: all $(SUBDIRS)

compose:
	docker compose up --build -d
