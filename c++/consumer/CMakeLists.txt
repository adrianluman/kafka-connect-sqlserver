cmake_minimum_required(VERSION 2.6)

project (CONSUMER)

set(CMAKE_INSTALL_RPATH "/usr/local/lib")
set(CMAKE_INSTALL_RPATH_USE_LINK_PATH TRUE)

SET(CMAKE_CXX_FLAGS "-std=c++11")

add_executable(Consumer Consumer.cpp)

add_definitions(-DBOOST_LOG_DYN_LINK)


target_link_libraries(BondClient -lcppkafka -lserdes -lrdkafka -lavro -ljson11)
