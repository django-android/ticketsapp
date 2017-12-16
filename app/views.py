from django.shortcuts import render
from django.http import HttpResponse
from django.views import View

import json
# Create your views here.

class Home(View):
    def get(self, request, *arg, **kwargs):
        c = {'foo': 'bar'}
        return HttpResponse(json.dumps(c), content_type='application/json')
