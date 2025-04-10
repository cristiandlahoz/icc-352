package org.wornux.urlshortener.api.grpc.v1;

import io.grpc.stub.StreamObserver;
import org.wornux.urlshortener.api.grpc.v1.UrlShortenerServiceGrpc.UrlShortenerServiceImplBase;
import org.wornux.urlshortener.api.grpc.v1.Urlshortener.CreateUrlRequest;
import org.wornux.urlshortener.api.grpc.v1.Urlshortener.CreateUrlResponse;
import org.wornux.urlshortener.api.grpc.v1.Urlshortener.ListUserUrlsRequest;
import org.wornux.urlshortener.api.grpc.v1.Urlshortener.ListUserUrlsResponse;

public class UrlshortenedGrpc extends UrlShortenerServiceImplBase {

  @Override
  public void createUrl(
      CreateUrlRequest request, StreamObserver<CreateUrlResponse> responseObserver) {
    super.createUrl(request, responseObserver);
  }

  @Override
  public void listUserUrls(
      ListUserUrlsRequest request, StreamObserver<ListUserUrlsResponse> responseObserver) {
    super.listUserUrls(request, responseObserver);
  }
}
