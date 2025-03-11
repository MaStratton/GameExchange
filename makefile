APIDIR = api
EMAILDIR = emailServer

all:
	$(MAKE) -C $(APIDIR)
	$(MAKE) -C $(EMAILDIR)
	
	docker compose up --build -d

.PHONY: all $(SUBDIRS)

compose:
	docker compose up --build -d
